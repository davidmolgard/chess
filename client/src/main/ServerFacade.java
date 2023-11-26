import RequestResultClasses.loginClasses.LoginRequest;
import RequestResultClasses.loginClasses.LoginResult;
import RequestResultClasses.registerClasses.RegisterRequest;
import RequestResultClasses.registerClasses.RegisterResult;

public class ServerFacade {
    public LoginResult login(LoginRequest loginRequest) { //FIXME

        return new LoginResult(0, "");
    }

    public RegisterResult register(RegisterRequest registerRequest) { //FIXME

        return new RegisterResult(0, "");
    }
}
