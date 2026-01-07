package com.medlink.be.constant;

public class Endpoint {

  public static final String PERSON_V1 = "/v1/persons";
  public static final String PERSON_BY_ID_V1 = "/v1/persons/{id}";
  private Endpoint() {
    throw new UnsupportedOperationException("Utility class");
  }
}
