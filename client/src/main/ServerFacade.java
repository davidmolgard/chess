import RequestResultClasses.createClasses.CreateRequest;
import RequestResultClasses.createClasses.CreateResult;
import RequestResultClasses.joinClasses.JoinRequest;
import RequestResultClasses.joinClasses.JoinResult;
import RequestResultClasses.listClasses.ListResult;
import RequestResultClasses.loginClasses.LoginRequest;
import RequestResultClasses.loginClasses.LoginResult;
import RequestResultClasses.logoutClasses.LogoutRequest;
import RequestResultClasses.logoutClasses.LogoutResult;
import RequestResultClasses.registerClasses.RegisterRequest;
import RequestResultClasses.registerClasses.RegisterResult;
import models.AuthToken;

public class ServerFacade {
    public LoginResult login(LoginRequest loginRequest) { //FIXME

        return new LoginResult(0, "");
    }

    public RegisterResult register(RegisterRequest registerRequest) { //FIXME

        return new RegisterResult(0, "");
    }

    public LogoutResult logout(LogoutRequest logoutRequest) { //FIXME

        return new LogoutResult();
    }

    public ListResult list(AuthToken authToken) { //FIXME

        return new ListResult(0, "");
    }

    public CreateResult create(CreateRequest createRequest) { //FIXME

        return new CreateResult(0);
    }

    public JoinResult join(JoinRequest joinRequest) { //FIXME

        return new JoinResult();
    }
}
