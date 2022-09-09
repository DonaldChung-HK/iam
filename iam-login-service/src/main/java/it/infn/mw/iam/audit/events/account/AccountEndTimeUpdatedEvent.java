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
package it.infn.mw.iam.audit.events.account;

import java.util.Date;

import it.infn.mw.iam.persistence.model.IamAccount;

public class AccountEndTimeUpdatedEvent extends AccountEvent {

  private final Date previousEndTime;
  
  public AccountEndTimeUpdatedEvent(Object source, IamAccount account, Date previousEndTime,
      String message) {
    super(source, account, message);
    this.previousEndTime = previousEndTime;
  }

  private static final long serialVersionUID = 1L;

  public Date getPreviousEndTime() {
    return previousEndTime;
  }
}
