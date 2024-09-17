import { LoginDataI } from "./login-data.model";

export interface LoginResponseI {
    operation: string;
    success: boolean;
    error: boolean;
    errorMessage: string;
    loginData: LoginDataI;
}