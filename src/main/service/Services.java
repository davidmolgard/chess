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

/**
 * Services contains methods for all services offered by the API
 */
public class Services {
    /**
     * clears the database if authorized to do so
     * @param req @see ClearRequest
     * @return @see ClearResult (includes error code if error occurred)
     */
    ClearResult clear(ClearRequest req) {
        return new ClearResult();
    }
    /**
     * creates game if authorized to do so
     * @param req @see CreateRequest
     * @return @see CreateResult (includes error code if error occurred)
     */
    CreateResult create(CreateRequest req) {
        return new CreateResult(0);
    }
    /**
     * joins game if authorized to do so
     * @param req @see JoinRequest
     * @return @see JoinResult (includes error code if error occurred)
     */
    JoinResult join(JoinRequest req) {
        return new JoinResult();
    }
    /**
     * Lists all games if authorized to do so
     * @param req @see ListRequest
     * @return @see ListResult (includes error code if error occurred)
     */
    ListResult list(ListRequest req) {
        return new ListResult(200, "");
    }
    /**
     * Login user if authorized to do so
     * @param req @see LoginRequest
     * @return @see LoginResult (includes error code if error occurred)
     */
    LoginResult login(LoginRequest req) {
        return new LoginResult(0, "");
    }
    /**
     * Logout user if authorized to do so
     * @param req @see LogoutRequest
     * @return @see LogoutResult (includes error code if error occurred)
     */
    LogoutResult logout(LogoutRequest req) {
        return new LogoutResult();
    }
    /**
     * registers user if authorized to do so
     * @param req @see RegisterRequest
     * @return @see RegisterResult (includes error code if error occurred)
     */
    RegisterResult register(RegisterRequest req) {
        return new RegisterResult(0, "");
    }
}
