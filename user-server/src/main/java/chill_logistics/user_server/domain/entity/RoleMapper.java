package chill_logistics.user_server.domain.entity;

import lib.entity.Role;

public final class RoleMapper {

    // User 서버의 UserRole -> lib의 Role (JWT용)
    public static Role toLibRole(UserRole userRole) {

        return switch (userRole){
            case MASTER -> Role.MASTER;
            case HUB_MANAGER -> Role.HUB_MANAGER;
            case DELIVERY_MANAGER -> Role.DELIVERY_MANAGER;
            case FIRM_MANAGER -> Role.FIRM_MANAGER;
        };
    }

    // 반대 방향용 (필요할 시 사용)
    public static UserRole toUserRole(Role role) {
        return switch (role){
            case MASTER -> UserRole.MASTER;
            case HUB_MANAGER -> UserRole.HUB_MANAGER;
            case DELIVERY_MANAGER -> UserRole.DELIVERY_MANAGER;
            case FIRM_MANAGER -> UserRole.FIRM_MANAGER;
        };
    }
}
