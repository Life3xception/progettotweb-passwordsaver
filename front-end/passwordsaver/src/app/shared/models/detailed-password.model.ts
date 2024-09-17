import { PasswordI } from "./password.model";

export interface DetailedPasswordI extends PasswordI {
    serviceName: string;
}