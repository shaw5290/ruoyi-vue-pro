package cn.iocoder.yudao.module.project.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * 项目模块错误码
 *
 * 使用 1-061-000-000 段。
 */
public interface ErrorCodeConstants {

    ErrorCode PROJECT_NOT_EXISTS = new ErrorCode(1_061_000_000, "项目不存在");
    ErrorCode PROJECT_CODE_DUPLICATE = new ErrorCode(1_061_000_001, "项目编号已存在");
    ErrorCode PROJECT_HAS_SOLUTION = new ErrorCode(1_061_000_002, "项目存在项目方案，不能删除");
    ErrorCode PROJECT_HAS_TASK = new ErrorCode(1_061_000_003, "项目存在项目任务，不能删除");

    ErrorCode PROJECT_TASK_NOT_EXISTS = new ErrorCode(1_061_005_000, "项目任务不存在");

    ErrorCode PROJECT_ATTACHMENT_NOT_EXISTS = new ErrorCode(1_061_010_000, "项目附件不存在");
    ErrorCode PROJECT_ATTACHMENT_BOM_GROUP_NOT_EXISTS = new ErrorCode(1_061_010_001, "附件所属 BOM 分组不存在");
    ErrorCode PROJECT_ATTACHMENT_OWNER_MISMATCH = new ErrorCode(1_061_010_002, "附件所属项目与 BOM 分组不一致");

    ErrorCode PROJECT_SOLUTION_NOT_EXISTS = new ErrorCode(1_061_100_000, "项目方案不存在");
    ErrorCode PROJECT_SOLUTION_NAME_DUPLICATE = new ErrorCode(1_061_100_001, "同一项目下的方案名称已存在");

}
