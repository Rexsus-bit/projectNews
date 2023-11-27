package com.main.mainserver.model.user;

public enum Permission {

    BASIC("general_activity"),
    PUBLISH("publish_activity"),
    ADMIN("admin_activity");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

}
