package com.bielinskim;

public class Role {
    String roleName;
    Actor actor;

    public Role(String roleName, Actor actor) {
        this.roleName = roleName;
        this.actor = actor;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    @Override
    public String toString() {
        return "Role:" +
                "roleName='" + roleName + '\'' +
                ", actor=" + actor;
    }
}
