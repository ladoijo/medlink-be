package com.medlink.be.exception;

import com.medlink.be.util.LanguageUtil;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String errCode) {
    super(LanguageUtil.getMessage(errCode));
  }
}
