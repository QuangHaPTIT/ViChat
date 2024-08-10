import React, { useState } from "react";
import { BsArrowLeft, BsCheck2 } from "react-icons/bs";
import { Button } from "@mui/material";
import CircularProgress from "@mui/material/CircularProgress";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { createGroupChat } from "../../Redux/Chat/Action";
import { updateUser } from "../../Redux/Auth/Action";
import Avatar from "@mui/material/Avatar";
const NewGroup = ({groupMembers, setIsGroup, setNewGroup}) => {
  const [isImageUploading, setIsImageUploading] = useState(false);
  const [groupName, setGroupName] = useState();
  const [groupImage, setGroupImage] = useState();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const uploadToCloudnary = (pics) => {
    if(pics !== null) {
      setIsImageUploading(true);
    }
    setIsImageUploading(true);
    const data = new FormData();
    data.append("file", pics);
    data.append("upload_preset", "hadev2003");
    data.append("cloud_name", "dsttrke4u");

    fetch("https://api.cloudinary.com/v1_1/dsttrke4u/image/upload", {
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
        console.log("imgurl", data);
        const imageUrl = data?.url?.toString();
        if (imageUrl) {
            setGroupImage(imageUrl);
            setIsImageUploading(false);
        } else {
            console.error("Image URL not found in response data");
        }
    })
    .catch((error) => {
        console.error("Error uploading image:", error);
    });
}
  const handleCreateGroup = () => {
    console.log("handle create group")
    let userIds = [];
    for(let user of groupMembers) {
      userIds.push(user.id)
     
    }
    const group = {
      userIds,
      chat_name: groupName,
      chat_image: groupImage
    }
    const data = {
      token: localStorage.getItem("token"),
      group: group
    }
    dispatch(createGroupChat(data))
    setIsGroup(false)
    setNewGroup(false)
  }
  return (
    <div className="w-full h-full">
      <div className="flex items-center space-x-10 bg-[#008069] text-white pt-16 px-10 pb-5">
        <BsArrowLeft className="cursor-pointer text-2xl font-bold"/>
        <p className="text-xl font-semibold">New Group</p>
      </div>

      <div className="flex flex-col justify-center items-center my-10">
        <label htmlFor="imgInput" className="relative">
          {/* <img className="w-1/2 h-auto mx-auto"
            src={groupImage || "https://cdn.pixabay.com/photo/2016/11/14/17/39/group-1824145_640.png"}
            alt=""
          /> */}
          <Avatar alt="group name" sx={{width : "12rem", height: "12rem"}}  src={groupImage || "https://cdn.pixabay.com/photo/2016/11/14/17/39/group-1824145_640.png"} />
          {isImageUploading && (
            <CircularProgress className="absolute top-[5rem] left-[6rem]" />
          )}
        </label>
        <input id="imgInput" type="file" className="hidden"
        onChange={(e) => uploadToCloudnary(e.target.files[0])}
        // value={""}
        />
      </div>

      <div className="w-full flex justify-between items-center py-2 px-5">
        <input
          className="w-full outline-none border-b-2 border-green-700 px-2 bg-transparent"
          placeholder="Group Subject"
          value={groupName}
          type="text"
          onChange={(e) => setGroupName(e.target.value)}
        />
      </div>

      {groupName && (
        <div className="py-10 bg-slate-200 flex items-center justify-center">
          <Button onClick={handleCreateGroup}>
            <div className="bg-[#0c977d] rounded-full p-4">
              <BsCheck2 className="text-white font-bold text-3xl" />
            </div>
          </Button>
        </div>
      )}
    </div>
  );
};

export default NewGroup;
