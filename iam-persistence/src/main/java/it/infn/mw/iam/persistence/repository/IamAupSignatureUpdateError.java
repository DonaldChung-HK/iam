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
package it.infn.mw.iam.persistence.repository;

import it.infn.mw.iam.persistence.model.IamAccount;

public class IamAupSignatureUpdateError extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public IamAupSignatureUpdateError(IamAccount account) {
    super(String.format("As user '%s' is a service account, AUP signature operation not allowed", account.getUsername()));
  }
}
