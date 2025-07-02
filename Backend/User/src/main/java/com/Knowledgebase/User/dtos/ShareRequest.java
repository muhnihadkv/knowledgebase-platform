package com.Knowledgebase.User.dtos;

public class ShareRequest {
    private int userId;        // The user to add or remove
    private String permission; // "VIEW" or "EDIT"

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getPermission() { return permission; }
    public void setPermission(String permission) { this.permission = permission; }
}
