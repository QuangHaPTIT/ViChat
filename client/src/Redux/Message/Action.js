import { BASE_API_URL } from "../../Config/api"
import { CREATE_NEW_MESSAGE, GET_ALL_MESSAGES } from "./ActionType"

export const createMessage = (messageData) => async (dispatch) => {
   console.log(JSON.stringify(messageData.data))
    try {
        
        const res = await fetch(`${BASE_API_URL}/api/messages/create`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${messageData.token}`,
            },
            body: JSON.stringify(messageData.data),
        })
        const data = await res.json()
        console.log("create message", data)
        dispatch({type: CREATE_NEW_MESSAGE, payload: data})
    } catch (error) {
        console.log(error)
    }
}

export const getAllMessages = (resData) => async (dispatch) => {
    try {
        
        const res = await fetch(`${BASE_API_URL}/api/messages/chat/${resData.chatId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${resData.token}`,
            },
            
        })
        const data = await res.json()
        console.log("all message", data)
        dispatch({type: GET_ALL_MESSAGES, payload: data})
    } catch (error) {
        console.log(error)
    }
}