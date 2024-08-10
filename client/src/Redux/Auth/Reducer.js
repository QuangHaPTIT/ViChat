import { updateUser } from "./Action"
import { REGISTER, LOGIN, REQ_USER, SEARCH_USER, UPDATE_USER, LOGIN_WITH_GOOGLE } from "./ActionType"

const inititalValue = {
    signup:null,
    signin:null,
    reqUser:null,
    updateUser:null,
}

export const authReducer = (store=inititalValue, {type, payload}) => {
    if(type===REGISTER) {
        return{...store, signup: payload}
    }else if(type===LOGIN){
        return{...store, signin: payload}
    }else if(type===REQ_USER){
        return{...store, reqUser: payload}
    }else if(type===SEARCH_USER){
        return{...store, searchUser: payload}
    }else if(type===UPDATE_USER){
        return{...store, updatedUser: payload}
    }else if(type===LOGIN_WITH_GOOGLE){
        return{...store, loginWithGoogle: payload}
    }
    return store;
}