import { UserTypeI } from "./user-type.model";

export interface LoginDataI {
    username: string;
    token: string;
    userType: string;
}