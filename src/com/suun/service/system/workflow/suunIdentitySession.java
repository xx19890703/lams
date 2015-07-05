package com.suun.service.system.workflow;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jbpm.api.JbpmException;
import org.jbpm.api.identity.Group;
import org.jbpm.api.identity.User;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.id.DbidGenerator;
import org.jbpm.pvm.internal.identity.spi.IdentitySession;

public class suunIdentitySession implements IdentitySession {

	protected Session session;

	public void suunIdentitySessionImpl() {
		this.session = EnvironmentImpl.getFromCurrent(Session.class);
	}

	public String createH() {
		return null;		/*
		 * Test t = new Test();
		 * 
		 * long dbid = EnvironmentImpl.getFromCurrent(DbidGenerator.class)		 * 
		 * .getNextId();		 * 
		 * t.setDbid(dbid);		 * 
		 * t.setId("abc");
		 */
	}

	@Override
	@SuppressWarnings("unused")
	public String createUser(String id, String userName,
	     String businessEmail, String familName) {
		suunUser user = new suunUser(id, userName, businessEmail);
		long dbid = EnvironmentImpl.getFromCurrent(DbidGenerator.class).getNextId();
		session.save(user);
		return user.getId();
	}

	@Override
	public suunUser findUserById(String userId) {
		return (suunUser) session.createCriteria(suunUser.class).add(
		Restrictions.eq("id", userId)).uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> findUsersById(String... userIds) {
		List<User> users = session.createCriteria(suunUser.class).add(
		Restrictions.in("id", userIds)).list();
		if (userIds.length != users.size()) {
			throw new JbpmException("not all users were found: "
			+ Arrays.toString(userIds));
		}
		return users;

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> findUsers() {
		return session.createCriteria(suunUser.class).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void deleteUser(String userId) {
		// lookup the user
		suunUser user = findUserById(userId);
		// cascade the deletion to the memberships
		List<suunGroupMember> memberships = session.createCriteria(
		suunGroupMember.class).add(Restrictions.eq("user", user)).list();
		// delete the related memberships
		for (suunGroupMember membership : memberships) {
			session.delete(membership);
		}
		// delete the user
		session.delete(user);
	}

	@Override
	public String createGroup(String groupName, String groupType,
                	String parentGroupId) {
		suunGroup group = new suunGroup();
		String groupId = groupType != null ? groupType + "." + groupName: groupName;
		group.setId(groupId);
		long dbid = EnvironmentImpl.getFromCurrent(DbidGenerator.class).getNextId();
		group.setDbid(dbid);
		group.setGroupName(groupName);
		group.setGroupType(groupType);
		if (parentGroupId != null) {
			suunGroup parentGroup = findGroupById(parentGroupId);
			group.setParentGroup(parentGroup);
		}
		session.save(group);
		return group.getId();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> findUsersByGroup(String groupId) {
		return session.createCriteria(suunGroupMember.class).createAlias(
		"group", "g").add(Restrictions.eq("g.id", groupId))
		.setProjection(Projections.property("user")).list();
	}

	@Override
	public suunGroup findGroupById(String groupId) {
		return (suunGroup) session.createCriteria(suunGroup.class).add(
		Restrictions.eq("id", groupId)).uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Group> findGroupsByUserAndGroupType(String userId,String groupType) {
		return session.createQuery(
		"select distinct m.group" + " from "
		+ suunGroupMember.class.getName()
		+ " as m where m.user.id = :userId"
		+ " and m.group.type = :groupType").setString("userId",
		userId).setString("groupType", groupType).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Group> findGroupsByUser(String userId) {
		List<Group> gList = session.createQuery(
		"select distinct m.group" + " from "
		+ suunGroupMember.class.getName()
		+ " as m where m.user.id = :userId").setString(
		"userId", userId).list();
		return gList;

	}

	@SuppressWarnings("unchecked")
	public List<Group> findGroups() {
		return session.createCriteria(suunGroup.class).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void deleteGroup(String groupId) {
		// look up the group
		suunGroup group = findGroupById(groupId);
		// cascade the deletion to the memberships
		List<suunGroupMember> memberships = session.createCriteria(
		suunGroupMember.class).add(Restrictions.eq("group", group)).list();
		// delete the related memberships
		for (suunGroupMember membership : memberships) {
			session.delete(membership);
		}
		// delete the group
		session.delete(group);
	}

	@Override
	public void createMembership(String userId, String groupId, String role) {
		suunUser user = findUserById(userId);
		if (user == null) {
			throw new JbpmException("user " + userId + " doesn't exist");
		}
		suunGroup group = findGroupById(groupId);
		if (group == null) {
			throw new JbpmException("group " + groupId + " doesn't exist");
		}
		suunGroupMember membership = new suunGroupMember();
		membership.setUser(user);
		membership.setGroup(group);
		membership.setRole(role);
		long dbid = EnvironmentImpl.getFromCurrent(DbidGenerator.class).getNextId();
		membership.setDbid(dbid);
		session.save(membership);
	}

	@Override
	public void deleteMembership(String userId, String groupId, String role) {
		suunGroupMember membership = (suunGroupMember) session.createCriteria(
		suunGroupMember.class).createAlias("user", "u").createAlias(
		"group", "g").add(Restrictions.eq("u.id", userId)).add(
		Restrictions.eq("g.id", groupId)).uniqueResult();
		session.delete(membership);
	}

}
