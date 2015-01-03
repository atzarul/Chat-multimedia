package com.is.chatmultimedia.client;

import com.is.chatmultimedia.models.User;

public interface AuthenticationListener {

  public void loginSuccesfull(User user);

  public void loginFailed(String message);

  public void logout(String message);

}
