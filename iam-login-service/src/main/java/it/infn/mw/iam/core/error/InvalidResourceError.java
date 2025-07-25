/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2016-2021
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.infn.mw.iam.core.error;

import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;

@SuppressWarnings("deprecation")
public class InvalidResourceError extends ClientAuthenticationException {

  private static final long serialVersionUID = 1L;

  public InvalidResourceError(String message) {
    super(message);
  }

  @Override
  public String getOAuth2ErrorCode() {
    return "invalid_target";
  }
}
