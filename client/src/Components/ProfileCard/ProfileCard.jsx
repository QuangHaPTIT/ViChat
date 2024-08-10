import React from 'react'
import { BsArrowLeft, BsCheck2, BsPencil } from 'react-icons/bs'
import { useState } from 'react'
import { useDispatch, useSelector } from "react-redux";
import { updateUser } from '../../Redux/Auth/Action';
const ProfileCard = ({handleCloseOpenProfile, setName}) => {
    const [flag, setFlag] = useState(false)
    
    const {auth} = useSelector(store => store)
    const [username, setUsername] = useState(auth.reqUser?.result?.full_name)
    const dispatch = useDispatch();
    const handleFlag = () => {
        setFlag(true)
    }
    const handleCheckClick = () => {
        setFlag(false)
        const data = {
            token: localStorage.getItem("token"),
            data: { full_name: username },
        }

            dispatch(updateUser(data));
        
    }
    const handleChange = (e) => {
        console.log(e.target.value)
        setUsername(e.target.value)
        setName(e.target.value)
    }
    const handleUpdateName = (e) => {
        const data = {
            token: localStorage.getItem("token"),
            data: { full_name: username },
        }
        if(e.target.key === "Enter") {
            dispatch(updateUser(data));
        }
    }
    const [tempPicture, setTempPicture] = useState(null);
    const uploadToCloudnary = (pics) => {
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
                setTempPicture(imageUrl);
                const dataa = {
                    token: localStorage.getItem("token"),
                    data: { profile_picture: imageUrl },
                };
                dispatch(updateUser(dataa));
            } else {
                console.error("Image URL not found in response data");
            }
        })
        .catch((error) => {
            console.error("Error uploading image:", error);
        });
    }
    
  return (
    <div className='w-full h-full'>
        <div className='flex items-center space-x-10 bg-[#008069] text-white pt-16 px-10 pb-5'>
            <BsArrowLeft className='cursor-pointer text-2xl font-bold' onClick={handleCloseOpenProfile}/>
            <p className='cursor-pointer font-semibold'>Profile</p>
        </div>
        {/* update profile pic section */}
        <div className='flex flex-col justify-center items-center my-12'>
            <label htmlFor="imgInput">
                <img className='rounded-full w-[15vw] h-[15vw] cursor-pointer'
                 src={auth.reqUser?.result?.profile_picture || tempPicture || "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png"} alt="" />
            </label>
            <input onChange={(e) => uploadToCloudnary(e.target.files[0])} type="file" id='imgInput' className='hidden' />
        </div>
        {/* name section */}
        <div className='bg-white px-3'>
            <p className='py-3'>Your Name</p>
            {!flag && <div className='w-full flex justify-between items-center'>
                <p className='py-3'>{username || "username"}</p>
                <BsPencil onClick={handleFlag} className='cursor-pointer'/>
            </div>}

            {
                flag && <div className='w-full flex justify-between items-center py-2'>
                    <input onKeyPress={handleUpdateName} onChange={handleChange} type="text" className='w-[80] ouline-none border-b-2 border-blue-700 p-2' placeholder='Enter your name'/>
                    <BsCheck2 onClick={handleCheckClick} className='cursor-pointer text-2xl'/>
                </div>
            }
        </div>
        <div className='px-3 my-5'>
            <p className='py-10'>this is not your username, this name will be visible to your whatapp contects</p>
        </div>
    </div>
  )
}

export default ProfileCard