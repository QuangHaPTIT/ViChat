import React, { useEffect, useRef, useState } from "react";
import { TbCircleDashed } from "react-icons/tb";
import { BiCommentDetail } from "react-icons/bi";
import { AiOutlineSearch } from "react-icons/ai";
import {
  BsFilter,
  BsThreeDotsVertical,
  BsEmojiSmile,
  BsMicFill,
} from "react-icons/bs";
import { IoVideocamOutline } from "react-icons/io5";
import ChatCard from "./ChatCard/ChatCard";
import MessageCard from "./MessageCard/MessageCard";
import { ImAttachment } from "react-icons/im";
import "./HomePage.css";
import ProfileCard from "./ProfileCard/ProfileCard";
import { useNavigate } from "react-router-dom";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import CreateGroup from "./Group/CreateGroup";
import { useDispatch, useSelector } from "react-redux";
import {
  currentUser,
  logout,
  logoutAction,
  searchUser,
  updateUser,
} from "../Redux/Auth/Action";
import { createChat, getUsersChat } from "../Redux/Chat/Action";
import { createMessage, getAllMessages } from "../Redux/Message/Action";
import SockJS from "sockjs-client";
import { over } from "stompjs";
const HomePage = () => {
  const [querys, setQuerys] = useState(null);
  const [currentChat, setCurrentChat] = useState(null);
  const [content, setContent] = useState("");
  const [isProfile, setIsProfile] = useState(false);
  const fileInputRef = useRef(null);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { auth, chat, message } = useSelector((store) => store);
  const [name, setName] = useState(auth.reqUser?.result.full_name);
  const [stompClient, setStompClient] = useState();
  const [isConnect, setIsConnect] = useState(false);
  const [messages, setMessages] = useState([]);
  const token = localStorage.getItem("token");
  const handleClickOnChatCard = (user_id) => {
    console.log(user_id);
    dispatch(createChat({ data: { user_id }, token }));
    setQuerys("");
  };
  const handleSearch = (keyword) => {
    dispatch(searchUser({ keyword, token }));
  };
  const handleCreateMessage = () => {
    const user_id =
      auth.reqUser?.result.id === currentChat?.users[0].id
        ? currentChat.users[1].id
        : currentChat.users[0].id;
    if (content.trim() !== "")
      dispatch(
        createMessage({
          data: { chat_id: currentChat.id, content: content, user_id: user_id },
          token: token,
        })
      );
  };

  const handleNavigate = () => {
    setIsProfile(true);
    // navigate("/profile");
  };
  const handleCloseOpenProfile = () => {
    setIsProfile(false);
  };

  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);

  const connect = () => {
    const sock = new SockJS("http://localhost:5454/api/ws");
    const temp = over(sock);
    setStompClient(temp);
    const headers = {
      Authorization: `${token}`,
      "X-XSRF-TOKEN": getCookie("XSRF-TOKEN"),
    };
    temp.connect(headers, onConnect, onError);
  };
  //   const connect = () => {
  //     const sock = new SockJS("http://localhost:5454/api/ws");
  //     const temp = over(sock);
  //     setStompClient(temp);
  //     temp.connect({}, onConnect, onError); // Không sử dụng headers
  // };

  function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) {
      return parts.pop().split(";").shift();
    }
  }

  const onError = (error) => {
    console.log("no Error", error);
  };
  const onConnect = () => {
    setIsConnect(true);
  };
  useEffect(() => {
    connect();
  }, []);
  useEffect(() => {
    if (message.newMessage && stompClient) {
      setMessages((prevMessages) => {
        const validMessages = Array.isArray(prevMessages) ? prevMessages : [];
        return [...validMessages, message.newMessage.result];
      });

      console.log("sending", messages);
      console.log("new message sending", message.newMessage?.result);
      stompClient.send(
        "/app/message",
        {},
        JSON.stringify(message.newMessage?.result)
      );
    }
  }, [message.newMessage?.result]);

 

  const onMessageReceived = (payload) => {
    console.log("Payload received:", payload); // Kiểm tra payload nhận được
    const receivedMessage = JSON.parse(payload.body); // Phân tích tin nhắn từ payload
    console.log("Parsed message:", receivedMessage); // Kiểm tra tin nhắn đã phân tích
    setMessages([...messages, receivedMessage]); // Cập nhật tin nhắn
    console.log("Updated messages:", messages); // Ghi log trạng thái tin nhắn
  };

  useEffect(() => {
    if (isConnect && stompClient && auth.reqUser && currentChat) {
      const subscription = stompClient.subscribe(
        `/group/${currentChat.id.toString()}`,
        onMessageReceived
      );
      console.log("currentId", currentChat.id.toString());
      console.log("subscription", subscription);
      return () => {
        if (subscription) {
          subscription.unsubscribe();
        }
      };
    }
  });

  useEffect(() => {
    setMessages(message.messages?.result);
    console.log("usefecc messages", message.messages?.result);
  }, [message.messages?.result]);

  const handleClick = (e) => {
    setAnchorEl(e.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleCreateGroup = () => {
    setIsGroup(true);
  };

  useEffect(() => {
    dispatch(getUsersChat({ token }));
  }, [chat.createdChat, chat.createdGroup]);

  useEffect(() => {
    if (currentChat?.id)
      dispatch(getAllMessages({ chatId: currentChat.id, token }));
  }, [currentChat, message.newMessage?.result]);
  useEffect(() => {
    dispatch(currentUser(token));
  }, [token]);

  const handleLogout = () => {
    dispatch(logoutAction());
    navigate("/signin");
  };
  useEffect(() => {
    if (!auth.reqUser) {
      navigate("/signin");
    }
  }, [auth.reqUser?.result]);

  const handleCurrentChat = (item) => {
    setCurrentChat(item);
  };
  const [isGroup, setIsGroup] = useState(false);

  const handleAttachment = (e) => {
    e.preventDefault();
    // Kích hoạt input khi biểu tượng được nhấp
    fileInputRef.current.click();
  };
  const handleFileChange = (event) => {
    // Làm gì đó với tệp đã chọn, ví dụ: tải lên
    const files = Array.from(event.target.files); // Chuyển đổi FileList thành mảng
    uploadToCloudinary(files);
    if (files.length > 0) {
      // xử lí upload file lên s3
      console.log('Selected file:', files[0]);
    }
  };
  const uploadToCloudinary = (files) => {
    const promises = files.map((file) => {
      const data = new FormData();
      data.append("file", file);
      data.append("upload_preset", "hadev2003");
      data.append("cloud_name", "dsttrke4u");
  
      return fetch("https://api.cloudinary.com/v1_1/dsttrke4u/image/upload", {
        method: "POST",
        body: data,
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          return response.json();
        })
        .then((data) => {
          const imageUrl = data?.url?.toString();
          if (!imageUrl) {
            throw new Error("Image URL not found in response data");
          }
          return imageUrl; // Trả về URL của ảnh đã tải lên
        });
    });
  
    // Sử dụng Promise.all để đợi tất cả các promises hoàn thành
    Promise.all(promises)
      .then((urls) => {
        console.log("All images uploaded successfully:", urls);
        // Lưu trữ tất cả các URL ảnh vào state hoặc làm gì đó khác
        const data = {
          token: localStorage.getItem("token"),
          data: { profile_pictures: urls }, // Lưu nhiều URL ảnh
        };
        dispatch(updateUser(data));
      })
      .catch((error) => {
        console.error("Error uploading images:", error);
      });
  };
  
  // Gọi hàm uploadToCloudinary với mảng files
  const handleFiles = (event) => {
    
  };
  
  return (
    <div className="relative">
      <div className=" w-full py-14 bg-[#00a884]"></div>
      <div className="flex bg-[#f0f2f5] h-[90vh] absolute top-[5vh] left-[2vw] w-[96vw]">
        <div className="left w-[30%] bg-[#e8e9ec] h-full">
          {/* profile */}
          {isProfile && (
            <div className="w-full h-full">
              <ProfileCard
                setName={setName}
                handleCloseOpenProfile={handleCloseOpenProfile}
              />
            </div>
          )}
          {isGroup && <CreateGroup setIsGroup={setIsGroup} />}
          {!isProfile && !isGroup && (
            <div className="w-full">
              {/* home */}
              {
                <div className="flex justify-between items-center p-3">
                  <div
                    onClick={handleNavigate}
                    className="flex items-center space-x-3"
                  >
                    <img
                      className="rounded-full w-10 h-10 cursor-pointer"
                      src={
                        auth.reqUser?.result?.profile_picture ||
                        "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"
                      }
                      alt=""
                    />
                    <p>{name}</p>
                  </div>

                  <div className="space-x-3 text-2xl flex">
                    <TbCircleDashed
                      className="cursor-pointer"
                      onClick={() => navigate("/status")}
                    />
                    <BiCommentDetail className="cursor-pointer" />
                    <div>
                      <BsThreeDotsVertical
                        id="basic-button"
                        aria-controls={open ? "basic-menu" : undefined}
                        aria-haspopup="true"
                        aria-expanded={open ? "true" : undefined}
                        onClick={handleClick}
                        className="cursor-pointer"
                      />

                      <Menu
                        id="basic-menu"
                        anchorEl={anchorEl}
                        open={open}
                        onClose={handleClose}
                        MenuListProps={{
                          "aria-labelledby": "basic-button",
                        }}
                      >
                        <MenuItem onClick={handleClose}>Profile</MenuItem>
                        <MenuItem onClick={handleCreateGroup}>
                          Create Group
                        </MenuItem>
                        <MenuItem onClick={handleLogout}>Logout</MenuItem>
                      </Menu>
                    </div>
                  </div>
                </div>
              }
              <div className="relative flex justify-center items-center bg-white py-4 px-3">
                <input
                  className="border-none outline-none bg-slate-200 rounded-md w-[93%] pl-9 py-2"
                  type="text"
                  placeholder="Search or start new chat"
                  onChange={(e) => {
                    setQuerys(e.target.value);
                    handleSearch(e.target.value);
                  }}
                  value={querys}
                />
                <AiOutlineSearch className="left-6 top-7 absolute cursor-pointer" />
                <div>
                  <BsFilter className="ml-4 text-3xl cursor-auto" />
                </div>
              </div>
              {/*all user */}
              <div className="bg-white overflow-y-scroll h-[70vh] px-3">
                {querys &&
                  auth.searchUser?.result?.map((item, index) => (
                    <div onClick={() => handleClickOnChatCard(item.id)}>
                      {" "}
                      <hr />
                      <ChatCard
                        name={item.full_name}
                        key={index}
                        userImg={
                          item.profile_picture ||
                          "https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_640.png"
                        }
                      />
                    </div>
                  ))}

                {chat.chats?.result?.length > 0 &&
                  !querys &&
                  chat.chats?.result?.map((item, index) => (
                    <div onClick={() => handleCurrentChat(item)}>
                      <hr />
                      {item.is_group ? (
                        <ChatCard
                          key={index}
                          name={item.chat_name}
                          userImg={
                            item.chat_image ||
                            "https://cdn.pixabay.com/photo/2016/11/14/17/39/group-1824145_640.png"
                          }
                        />
                      ) : (
                        <ChatCard
                          isChat={true}
                          key={index}
                          name={
                            auth.reqUser?.result.id === item.users[0].id
                              ? item.users[1].full_name
                              : item.users[0].full_name
                          }
                          userImg={
                            auth.reqUser?.result.id !== item.users[0].id
                              ? item.users[0].profile_picture ||
                                "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"
                              : item.users[1].profile_picture ||
                                "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"
                          }
                        />
                      )}
                    </div>
                  ))}
              </div>
            </div>
          )}
        </div>
        {/* default whats up page page */}
        {!currentChat && (
          <div className="w-[70%] flex flex-col items-center justify-center h-full ">
            <div className="max-w-[70%] text-center ">
              <img
                src="https://res.cloudinary.com/zarmariya/image/upload/v1662264838/whatsapp_multi_device_support_update_image_1636207150180-removebg-preview_jgyy3t.png"
                alt=""
              />
              <h1 className="text-4xl text-gray-600">WhatsApp Web</h1>
              <p className="my-9">
                Send and reveive message without keeping your phone online. Use
                WhatsApp on Up to 4 Linked devices and 1 phone at the same time
              </p>
            </div>
          </div>
        )}
        {/* Message part */}

        {currentChat && (
          <div className="w-[70%] relative">
            <div className="header absolute top-0 w-full bg-[#f0f2f5]">
              <div className="flex justify-between">
                <div className="py-3 space-x-4 flex items-center px-3">
                  <img
                    className="w-10 h-10 rounded-full"
                    src={
                      currentChat.is_group
                        ? currentChat.chat_image ||
                          "https://cdn.pixabay.com/photo/2016/04/15/18/05/computer-1331579_640.png"
                        : auth.reqUser?.result.id === currentChat?.users[0].id
                        ? currentChat.users[1]?.profile_picture ||
                          "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"
                        : currentChat?.users[0]?.profile_picture ||
                          "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"
                    }
                    alt=""
                  />
                  <p>
                    {currentChat.is_group
                      ? currentChat.chat_name
                      : auth.reqUser?.result.id === currentChat?.users[0].id
                      ? currentChat.users[1]?.full_name
                      : currentChat?.users[0]?.full_name}
                  </p>
                </div>
                <div className="py-3 flex space-x-4 items-center px-3">
                  <AiOutlineSearch className="cursor-pointer" />
                  <BsThreeDotsVertical className="cursor-pointer" />
                  <IoVideocamOutline className="cursor-pointer" />
                </div>
              </div>
            </div>
            {/* message section */}
            <div className="px-10 h-[85vh] overflow-y-scroll bg-blue-200">
              <div className="space-y-1 flex flex-col justify-center mt-20 py-2">
                {/* {messages?.length > 0 && messages?.map((item, i) => (
                  
                  <MessageCard
                  
                    isReqUserMessage={item?.user?.id===auth?.reqUser?.result?.id}
                    content={item.content}
                  />
                  
                ))}  */}
                {messages?.length > 0 &&
                  messages.map((item, i) => (
                    <div key={i} className="flex flex-col">
                      <MessageCard
                        isReqUserMessage={
                          item?.user?.id === auth?.reqUser?.result?.id
                        }
                        content={item.content}
                      />
                      {item.medias && item.medias.length > 0 && (
                        <div className="media-container mt-1">
                          {item.medias.map((media, index) => (
                            <img
                              key={index}
                              src={media.url} // URL của ảnh
                              alt={`media-${index}`}
                              className="media-image w-40 h-40 object-cover rounded-lg cursor-pointer"
                            />
                          ))}
                        </div>
                      )}
                    </div>
                  ))}
              </div>
            </div>
            {/* footer part */}
            <div className="footer bg-[#f0f2f5] absolute bottom-0 w-full py-3 text-2xl">
              <div className="flex justify-between items-center px-5 relative">
                <BsEmojiSmile className="cursor-pointer" />
                <ImAttachment
                  type="file"
                  onClick={(e) => handleAttachment(e)}
                  className="cursor-pointer"
                />
                <input
                  type="file"
                  ref={fileInputRef}
                  onChange={handleFileChange}
                  style={{ display: "none" }} // Ẩn input
                />
                <input
                  className="h-6 py-0 outline-none border-none bg-white  rounded-md w-[85%] input-custom-placeholder text-lg"
                  type="text"
                  onChange={(e) => setContent(e.target.value)}
                  placeholder="Type a message"
                  value={content}
                  onKeyPress={(e) => {
                    if (e.key === "Enter") {
                      handleCreateMessage();
                      setContent("");
                    }
                  }}
                />
                <BsMicFill className="cursor-pointer" />
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
export default HomePage;
