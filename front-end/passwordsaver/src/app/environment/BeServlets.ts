export const BeMainApis = {
    login: 'login',
    logout: 'logout',
    passwords: 'passwords',
    users: 'users',
    usertypes: 'usertypes',
    services: 'services',
    servicetypes: 'servicetypes'
};

export const BeApis = {
    getpassword: `${BeMainApis.passwords}/getpassword`,
    addpassword: `${BeMainApis.passwords}/addpassword`,
    updatepassword: `${BeMainApis.passwords}/updatepassword`,
    deletepassword: `${BeMainApis.passwords}/deletepassword`,
    getuser: `${BeMainApis.users}/getuser`,
    adduser: `${BeMainApis.users}/adduser`,
    updateuser: `${BeMainApis.users}/updateuser`,
    deleteuser: `${BeMainApis.users}/deleteuser`,
    getusertype: `${BeMainApis.usertypes}/getusertype`,
    addusertype: `${BeMainApis.usertypes}/addusertype`,
    updateusertype: `${BeMainApis.usertypes}/updateusertype`,
    deleteusertype: `${BeMainApis.usertypes}/deleteusertype`,
    getservice: `${BeMainApis.services}/getservice`,
    addservice: `${BeMainApis.services}/addservice`,
    updateservice: `${BeMainApis.services}/updateservice`,
    deleteservice: `${BeMainApis.services}/deleteservice`,
    getservicetype: `${BeMainApis.servicetypes}/getservicetype`,
    addservicetype: `${BeMainApis.servicetypes}/addservicetype`,
    updateservicetype: `${BeMainApis.servicetypes}/updateservicetype`,
    deleteservicetype: `${BeMainApis.servicetypes}/deleteservicetype`
}