<%--

    Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2016-2019

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags/iam"%>
<t:page title="Sign Acceptable Usage Policy">
  <h2 class="text-center">Sign Acceptable Usage Policy</h2>
  <form id="sign-aup-form" class="sign-aup-form form" action="/iam/aup/sign" method="post">
    <p id="sign-aup-subtitle">In order to proceed, you need to declare that 
    you accept the terms of this organization <a href="${aup.url}">Acceptable Usage Policy (AUP)</a></p>
  
    <div class="form-group sign-aup-btns">
      <input id="sign-aup-btn" class="btn btn-success" type="submit" value="I agree with the AUP terms">
    </div>
  </form>
</t:page>
