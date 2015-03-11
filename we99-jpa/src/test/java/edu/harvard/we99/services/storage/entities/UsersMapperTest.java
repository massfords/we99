package edu.harvard.we99.services.storage.entities;

import edu.harvard.we99.security.Role;
import edu.harvard.we99.security.RoleName;
import edu.harvard.we99.security.User;
import org.junit.Test;

import java.util.Collections;

import static edu.harvard.we99.test.BaseFixture.assertJsonEquals;
import static edu.harvard.we99.test.BaseFixture.load;
import static edu.harvard.we99.util.JacksonUtil.toJsonString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * @author mford
 */
public class UsersMapperTest {
    RoleEntity testRole = new RoleEntity().withId(500L).withName(new RoleName("junit"));

    // we'll ignore this role when converting from domain to entity
    Role adminRoleFromClient = new Role(new RoleName(RoleName.BuiltIn.Admin.name()));

    @Test
    public void domainToEntity_existing() throws Exception {
        User user = makeUser();
        UserEntity ue = makeEntity();
        Mappers.USERS.mapReverse(user, ue);
        // password is unchanged
        assertEquals("abcd", ue.getPassword());
        // role is unchanged
        assertSame(testRole, ue.getRole());
        assertEquals(user.getId(), ue.getId());
        assertEquals("Jon123", ue.getFirstName());
        assertEquals("Smith456", ue.getLastName());
        assertEquals("foo789@example.com", ue.getEmail());
    }

    @Test
    public void domainToEntity_new() throws Exception {
        User user = makeUser();
        // mapping from domain to entity SHOULD NOT touch the ID or the RoleEntity
        UserEntity ue = Mappers.USERS.mapReverse(user);
        assertNull(ue.getRole());
        assertEquals(user.getId(), ue.getId());
        assertEquals("Jon123", ue.getFirstName());
        assertEquals("Smith456", ue.getLastName());
        assertEquals("foo789@example.com", ue.getEmail());
    }

    @Test
    public void entityToDomain() throws Exception {
        UserEntity ue = makeEntity();
        // mapping from entity to domain should include the id and role
        User user = Mappers.USERS.map(ue);
        assertJsonEquals(load("/Mappers/user.json"), toJsonString(user));
        // their experiment id's should have been copied over as well
        assertEquals(1, user.getExperiments().size());
    }

    private UserEntity makeEntity() {
        UserEntity userEntity = new UserEntity()
                .withId(250L)
                .withRole(testRole)
                .withFirstName("Jon")
                .withLastName("Smith")
                .withEmail("foo@example.com")
                .withPassword("abcd");
        // setup an experiment to test the unidirectional mapping of just the id's
        userEntity.setExperiments(Collections.singletonMap(123L, new ExperimentEntity("foo")));
        return userEntity;
    }

    private User makeUser() {
        User user = new User().withId(100L)
                .withFirstName("Jon123")
                .withLastName("Smith456")
                .withEmail("foo789@example.com")
                .withRole(adminRoleFromClient)
                .withPassword("foo");
        // the experiments shouldn't be mapped from a domain to an entity
        user.setExperiments(Collections.singleton(999L));
        return user;
    }

}
