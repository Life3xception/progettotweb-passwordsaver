export const environment = {
    apiEndpoint: 'http://localhost:8080/passwordsaver',
    accessTokenName: 'access_token',
    loginDataName: 'login_data',
    adminRole: 'ADMIN',
    key: 'PasswordSaverAngular',
    pwdRegex: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,50}$/,
    emailRegex: /[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}/,
}