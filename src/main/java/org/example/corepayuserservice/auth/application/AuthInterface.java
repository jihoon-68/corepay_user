package org.example.corepayuserservice.auth.application;


import org.example.corepayuserservice.auth.application.command.LoginCommand;
import org.example.corepayuserservice.auth.application.command.SignupCommand;
import org.example.corepayuserservice.auth.application.command.UpdatePasswordCommand;

public interface AuthInterface {
    String login(LoginCommand command);
    void signup(SignupCommand command);
    void updatePassword(UpdatePasswordCommand command);
}
