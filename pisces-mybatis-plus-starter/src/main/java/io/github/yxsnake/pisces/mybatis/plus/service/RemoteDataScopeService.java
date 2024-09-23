package io.github.yxsnake.pisces.mybatis.plus.service;

public interface RemoteDataScopeService {

    /**
     * 获取角色自定义权限语句
     *
     * @param roleId 角色ID
     * @return 返回角色的自定义权限语句，如果没有找到则返回 null
     */
    String getRoleCustom(Long roleId);

    /**
     * 获取部门和下级权限语句
     *
     * @param deptId 部门ID
     * @return 返回部门及其下级的权限语句，如果没有找到则返回 null
     */
    String getDeptAndChild(Long deptId);

}