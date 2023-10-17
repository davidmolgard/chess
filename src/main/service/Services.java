package service;

import service.clearClasses.ClearRequest;
import service.clearClasses.ClearResult;
import service.createClasses.CreateRequest;
import service.createClasses.CreateResult;
import service.joinClasses.JoinRequest;
import service.joinClasses.JoinResult;
import service.listClasses.ListRequest;
import service.listClasses.ListResult;
import service.loginClasses.LoginRequest;
import service.loginClasses.LoginResult;
import service.logoutClasses.LogoutRequest;
import service.logoutClasses.LogoutResult;
import service.registerClasses.RegisterRequest;
import service.registerClasses.RegisterResult;

public class Services {
    ClearResult clear(ClearRequest req) {
        return new ClearResult();
    }

    CreateResult create(CreateRequest req) {
        return new CreateResult(0);
    }

    JoinResult join(JoinRequest req) {
        return new JoinResult();
    }

    ListResult list(ListRequest req) {
        return new ListResult(200, "");
    }

    LoginResult login(LoginRequest req) {
        return new LoginResult(0, "");
    }

    LogoutResult logout(LogoutRequest req) {
        return new LogoutResult();
    }

    RegisterResult register(RegisterRequest req) {
        return new RegisterResult(0, "");
    }
}
