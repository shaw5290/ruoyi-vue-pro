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

    ErrorCode PROJECT_SOLUTION_NOT_EXISTS = new ErrorCode(1_061_100_000, "项目方案不存在");
    ErrorCode PROJECT_SOLUTION_NAME_DUPLICATE = new ErrorCode(1_061_100_001, "同一项目下的方案名称已存在");

}
