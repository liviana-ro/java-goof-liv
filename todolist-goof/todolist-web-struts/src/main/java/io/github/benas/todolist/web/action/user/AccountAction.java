/*
 * The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package io.github.benas.todolist.web.action.user;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.github.benas.todolist.web.action.BaseAction;
import io.github.benas.todolist.web.common.form.ChangePasswordForm;
import io.github.benas.todolist.web.common.form.RegistrationForm;
import io.github.benas.todolist.web.common.util.TodoListUtils;
import io.github.todolist.core.domain.User;

import javax.validation.ConstraintViolation;
import java.text.MessageFormat;
import java.util.Set;
import java.security.MessageDigest;

/**
 * Action class for Account CRUD operations.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class AccountAction extends BaseAction {

    private static final Logger LOGGER = LogManager.getLogger(AccountAction.class.getName());

    private ChangePasswordForm changePasswordForm;

    private RegistrationForm registrationForm;

    private User user;

    private String updateProfileSuccessMessage, updatePasswordSuccessMessage;

    private String error, errorName, errorEmail, errorPassword, errorNewPassword,
            errorCurrentPassword, errorConfirmationPassword, errorConfirmationPasswordMatching;

    /**
     * **************
     * Account details
     * ***************
     */

    public String account() {
        user = getSessionUser();
        return Action.SUCCESS;
    }

    /**
     * *******************
     * Register new account
     * *******************
     */

    public String register() {
        return Action.SUCCESS;
    }

    public String doRegister() {

        validateRegistrationForm();

        if (error != null) {
            return ActionSupport.INPUT;
        }

        if (isAlreadyUsed(registrationForm.getEmail())) {
            error = MessageFormat.format(getText("register.error.global.account"), registrationForm.getEmail());
            return ActionSupport.INPUT;
        }

        User user = new User(registrationForm.getName(), registrationForm.getEmail(), registrationForm.getPassword());
        user = userService.create(user);
        session.put(TodoListUtils.SESSION_USER, user);
        return Action.SUCCESS;
    }

    private boolean isAlreadyUsed(String email) {
        return userService.getUserByEmail(email) != null;
    }

    private void validateRegistrationForm() {
        validateName();

        validateEmail();

        validatePassword();

        validateConfirmationPassword();

        checkPasswordsMatch();
    }

    private void checkPasswordsMatch() {
        if (confirmationPasswordDoesNotMatchPassword()) {
