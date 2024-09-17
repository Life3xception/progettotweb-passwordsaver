import { UserI } from "./user.model";

export interface DetailedUserI extends UserI {
    userTypeName: string;
}