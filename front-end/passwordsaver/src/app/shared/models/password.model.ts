export class Password {
    idPassword!: number;
    name!: string;
    password!: string;
    email!: string;
    username!: string;
    idUser!: number;
    idService!: number;
    passPhrase!: string;
    isStarred!: boolean;
    validity: boolean = true;
    showed: boolean = false;
}