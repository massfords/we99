package edu.harvard.we99.domain.lists;

import edu.harvard.we99.security.User;

import java.util.List;

/**
 * @author mford
 */
public class Users extends AbstractList<User> {
    public Users() {}
    public Users(List<User> users) {super(users);}
}
