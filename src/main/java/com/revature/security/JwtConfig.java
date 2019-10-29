package com.revature.security;

import org.springframework.beans.factory.annotation.Value;

/** Provides configuration which will be used during JWT creation upon successful authentication. */
public class JwtConfig {

  /*
   * Indicates the name of the HTTP header that will contain the prefix and JWT as
   * a value
   */
  @Value("${security.jwt.header:Authorization}")
  private String header;

  /*
   * A prefix that will come before the JWT within the response header. Can be
   * used to help distinguish this application's Authorization header token from
   * similarly named headers/tokens from other applications.
   */
  @Value("${security.jwt.prefix:Bearer }")
  private String prefix;

  // Amount of time a token is valid, currently set to: 24 hours
  @Value("${security.jwt.expiration:#{24*60*60}}")
  private int expiration;

  // Used as a key with the encryption algorithm to generate JWTs
  @Value("${security.jwt.secret}")
  private String secret;

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public int getExpiration() {
    return expiration;
  }

  public void setExpiration(int expiration) {
    this.expiration = expiration;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }
}
