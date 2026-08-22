package cn.iocoder.yudao.module.project.bom.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {
    ErrorCode BOM_GROUP_NOT_EXISTS = new ErrorCode(1_062_000_000, "BOM 模块不存在");
    ErrorCode BOM_GROUP_CODE_DUPLICATE = new ErrorCode(1_062_000_001, "BOM 模块编码已存在");
    ErrorCode BOM_GROUP_PARENT_INVALID = new ErrorCode(1_062_000_002, "BOM 分组父子关系无效或形成循环");
    ErrorCode BOM_DEFAULT_GROUP_DELETE_FORBIDDEN = new ErrorCode(1_062_000_003, "默认分组不能删除");
    ErrorCode BOM_VERSION_NOT_EXISTS = new ErrorCode(1_062_100_000, "BOM 模块版本不存在");
    ErrorCode BOM_VARIANT_NOT_EXISTS = new ErrorCode(1_062_200_000, "BOM 选项不存在");
    ErrorCode BOM_SELECTION_DUPLICATE_GROUP = new ErrorCode(1_062_300_000, "同一方案不能重复选择同一 BOM 模块");
    ErrorCode BOM_SELECTION_MISMATCH = new ErrorCode(1_062_300_001, "BOM 模块、版本和选项不匹配");
    ErrorCode BOM_WMS_BOM_BINDING_DUPLICATE = new ErrorCode(1_062_400_000, "同一项目 BOM 不能重复关联同一个 WMS BOM");
    ErrorCode BOM_ITEM_NOT_EXISTS = new ErrorCode(1_062_500_000, "BOM 物料不存在");
}

