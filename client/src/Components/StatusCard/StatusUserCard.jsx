import React from 'react'
import { useNavigate } from 'react-router-dom'
const StatusUserCard = () => {
  const navigate = useNavigate();
  const handleNavigate = () => {
    navigate('/status/{userId}')
  }
  return (
    <div onClick={handleNavigate} className='flex items-center p-3 cursor-pointer'>
        <div>
            <img className='h-7 w-7 lg-w-10 lg:h-10 rounded-full' src="https://cdn.pixabay.com/photo/2024/01/23/14/37/couple-8527721_1280.png" alt="" />
        </div>
        <div className='ml-2 text-white'>
          <p>Pham Quang Ha</p>
        </div>
    </div>
  )
}

export default StatusUserCard