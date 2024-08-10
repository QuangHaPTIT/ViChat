import React, { useState } from "react";
import { BsArrowLeft, BsArrowRight } from "react-icons/bs";
import SelectedMember from "./SelectedMember";
import ChatCard from "../ChatCard/ChatCard";
import NewGroup from "./NewGroup";
import { useDispatch, useSelector} from "react-redux";
import {
  searchUser,
} from "../../Redux/Auth/Action";
import { store } from "../../Redux/store";
const CreateGroup = ({setIsGroup}) => {
  const [newGroup, setNewGroup] = useState(false);
  const [groupMembers, setGroupMembers] = useState(new Set());
  const [query, setQuery] = useState("");
  const {auth} = useSelector(store => store)
  const dispatch = useDispatch();
  const token = localStorage.getItem("token");
  const handleRemoveMember = (item) => {
    groupMembers.delete(item);
    setGroupMembers(groupMembers);
  };
  const handleSearch = () => {
    dispatch(searchUser({ keyword: query, token }));
  };
  return (
    <div className="w-full h-full">
      {!newGroup && (
        <div>
          {" "}
          <div className="flex items-center space-x-10 bg-[#008069] text-white pt-16 px-10 pb-5">
            <BsArrowLeft className="cursor-pointer text-2xl font-bold" />
            <p className="text-xl font-semibold">Add Group Participats</p>
          </div>
          <div className="relative bg-white py-4 px-3">
            <div className="flex space-x-2 flex-wrap space-y-1">
              {groupMembers.size > 0 &&
                Array.from(groupMembers).map((item) => (
                  <SelectedMember
                    handleRemoveMember={() => handleRemoveMember(item)}
                    member={item}
                  />
                ))}
            </div>
            <input
              type="text"
              onChange={(e) => {
                handleSearch(e.target.value);
                setQuery(e.target.value);
              }}
              className="outline-none border-b border-[#8888] p-2 w-[93%]"
              placeholder="Search user"
              value={query}
            />
          </div>
          <div className="bg-white overflow-y-scroll h-[50.2vh]">
            {query &&
              auth?.searchUser?.result?.map((item) => (
                <div
                  onClick={() => {
                    groupMembers.add(item);
                    setGroupMembers(groupMembers);
                    setQuery("");
                  }}
                  key={item?.id}
                >
                  <hr />
                  <ChatCard userImg={item.profile_picture} name= {item.full_name}/>
                </div>
              ))}
          </div>
          <div className="bottom-10 py-10 bg-slate-200 items-center justify-center flex">
            <div className="bg-green-600 rounded-full p-4 cursor-pointer"
              onClick={() => {
                setNewGroup(true);
              }}>
              <BsArrowRight className="text-white font-bold text-2xl"/>
            </div>
          </div>
        </div>
      )}

      {newGroup && <NewGroup setNewGroup={setNewGroup} setIsGroup={setIsGroup} groupMembers = {groupMembers}/>}
    </div>
  );
};

export default CreateGroup;
