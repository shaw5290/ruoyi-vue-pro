package cn.iocoder.yudao.module.project.bom.controller.admin.selection;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.project.bom.controller.admin.selection.vo.BomSelectionSaveReqVO;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.BomSolutionSelectionDO;
import cn.iocoder.yudao.module.project.bom.service.BomSelectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 项目方案 BOM 选配")
@RestController @RequestMapping("/bom/selection")
public class BomSelectionController {
    @Resource private BomSelectionService service;
    @PutMapping("/save") @Operation(summary = "保存项目方案的 BOM 模块选择")
    @PreAuthorize("@ss.hasPermission('bom:selection:write')")
    public CommonResult<Boolean> save(@Valid @RequestBody BomSelectionSaveReqVO reqVO) { service.saveSelections(reqVO); return success(true); }
    @GetMapping("/list") @Operation(summary = "获得项目方案的 BOM 模块选择")
    @PreAuthorize("@ss.hasPermission('bom:selection:query')")
    public CommonResult<List<BomSolutionSelectionDO>> list(@RequestParam Long projectSolutionId) { return success(service.getSelections(projectSolutionId)); }
}

