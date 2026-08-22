package cn.iocoder.yudao.module.project.bom.controller.admin.group;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.project.bom.controller.admin.group.vo.*;
import cn.iocoder.yudao.module.project.bom.dal.dataobject.*;
import cn.iocoder.yudao.module.project.bom.service.BomLibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - BOM 模块库")
@RestController @RequestMapping("/bom/library")
public class BomLibraryController {
    @Resource private BomLibraryService service;
    @PostMapping("/group/save") @Operation(summary = "新增或更新 BOM 模块")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Long> saveGroup(@Valid @RequestBody BomGroupSaveReqVO reqVO) { return success(service.saveGroup(reqVO)); }
    @DeleteMapping("/group/delete") @Operation(summary = "删除功能分组")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Boolean> deleteGroup(@RequestParam Long id) { service.deleteGroup(id); return success(true); }
    @GetMapping("/group/list") @Operation(summary = "获得 BOM 模块列表")
    @PreAuthorize("@ss.hasPermission('bom:library:query')")
    public CommonResult<List<BomGroupDO>> getGroups(@RequestParam Long projectId) { return success(service.getGroupList(projectId)); }
    @PostMapping("/version/save") @Operation(summary = "新增或更新模块版本")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Long> saveVersion(@Valid @RequestBody BomGroupVersionSaveReqVO reqVO) { return success(service.saveVersion(reqVO)); }
    @GetMapping("/version/list") @Operation(summary = "获得模块版本列表")
    @PreAuthorize("@ss.hasPermission('bom:library:query')")
    public CommonResult<List<BomGroupVersionDO>> getVersions(@RequestParam Long groupId) { return success(service.getVersionList(groupId)); }
    @PostMapping("/variant/save") @Operation(summary = "新增或更新 BOM 选项")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Long> saveVariant(@Valid @RequestBody BomVariantSaveReqVO reqVO) { return success(service.saveVariant(reqVO)); }
    @DeleteMapping("/variant/delete") @Operation(summary = "删除 BOM")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Boolean> deleteVariant(@RequestParam Long id) { service.deleteVariant(id); return success(true); }
    @GetMapping("/variant/list") @Operation(summary = "获得 BOM 选项列表")
    @PreAuthorize("@ss.hasPermission('bom:library:query')")
    public CommonResult<List<BomVariantDO>> getVariants(@RequestParam Long versionId) { return success(service.getVariantList(versionId)); }
    @PostMapping("/item/save") @Operation(summary = "按项目 BOM 分组新增或更新 BOM 物料")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Long> saveItem(@Valid @RequestBody BomItemSaveReqVO reqVO) { return success(service.saveItem(reqVO)); }
    @DeleteMapping("/item/delete") @Operation(summary = "删除 BOM 物料")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Boolean> deleteItem(@RequestParam Long id) { service.deleteItem(id); return success(true); }
    @PostMapping("/item/image/create") @Operation(summary = "新增 BOM 物料图片")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Long> createItemImage(@Valid @RequestBody BomItemImageCreateReqVO reqVO) {
        return success(service.createItemImage(reqVO));
    }
    @DeleteMapping("/item/image/delete") @Operation(summary = "删除 BOM 物料图片")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Boolean> deleteItemImage(@RequestParam Long id) {
        service.deleteItemImage(id); return success(true);
    }
    @PutMapping("/item/image/set-primary") @Operation(summary = "设置 BOM 物料首图")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Boolean> setPrimaryItemImage(@RequestParam Long id) {
        service.setPrimaryItemImage(id); return success(true);
    }
    @PostMapping("/product/save") @Operation(summary = "新增或更新项目商品")
    @PreAuthorize("@ss.hasPermission('project:product:write') or @ss.hasPermission('bom:library:write')")
    public CommonResult<Long> saveProduct(@Valid @RequestBody ProjectProductSaveReqVO reqVO) {
        return success(service.saveProduct(reqVO));
    }
    @DeleteMapping("/product/delete") @Operation(summary = "删除项目商品")
    @PreAuthorize("@ss.hasPermission('project:product:write') or @ss.hasPermission('bom:library:write')")
    public CommonResult<Boolean> deleteProduct(@RequestParam Long id) {
        service.deleteProduct(id); return success(true);
    }
    @GetMapping("/product/list") @Operation(summary = "获得项目商品列表")
    @PreAuthorize("@ss.hasPermission('project:product:query') or @ss.hasPermission('project:info:query') or @ss.hasPermission('bom:library:query')")
    public CommonResult<List<ProjectProductDO>> getProductList(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String keyword) {
        return success(service.getProductList(projectId, keyword));
    }
    @PutMapping("/item/product/bind") @Operation(summary = "BOM 零件关联商品")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Boolean> bindItemProduct(@RequestParam Long itemId, @RequestParam Long productId) {
        service.bindItemProduct(itemId, productId); return success(true);
    }
    @DeleteMapping("/item/product/unbind") @Operation(summary = "BOM 零件解除商品关联")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Boolean> unbindItemProduct(@RequestParam Long itemId, @RequestParam Long productId) {
        service.unbindItemProduct(itemId, productId); return success(true);
    }
    @PostMapping("/document/save") @Operation(summary = "新增或更新 BOM 分组 Markdown 文档")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Long> saveDocument(@Valid @RequestBody BomDocumentSaveReqVO reqVO) {
        return success(service.saveDocument(reqVO));
    }
    @DeleteMapping("/document/delete") @Operation(summary = "删除 BOM 分组文档")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Boolean> deleteDocument(@RequestParam Long id) {
        service.deleteDocument(id); return success(true);
    }
    @GetMapping("/document/list") @Operation(summary = "获得项目文档汇总或指定 BOM 分组文档")
    @PreAuthorize("@ss.hasPermission('bom:library:query')")
    public CommonResult<List<BomDocumentDO>> getDocumentList(@RequestParam Long projectId,
                                                              @RequestParam(required = false) Long bomGroupId) {
        return success(service.getDocumentList(projectId, bomGroupId));
    }
    @PutMapping("/item/bind-wms") @Operation(summary = "绑定 WMS 标准物料并重置 BOM 标准字段")
    @PreAuthorize("@ss.hasPermission('bom:library:write') and @ss.hasPermission('wms:item:query')")
    public CommonResult<Boolean> bindWmsItem(@Valid @RequestBody BomItemBindWmsReqVO reqVO) {
        service.bindWmsItem(reqVO); return success(true);
    }
    @PutMapping("/item/unbind-wms") @Operation(summary = "解除 WMS 物料绑定并保留快照")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Boolean> unbindWmsItem(@RequestParam Long id) {
        service.unbindWmsItem(id); return success(true);
    }
    @GetMapping("/item/list") @Operation(summary = "获得 BOM 物料明细")
    @PreAuthorize("@ss.hasPermission('bom:library:query')")
    public CommonResult<List<BomItemDO>> getItems(@RequestParam Long variantId) { return success(service.getItemList(variantId)); }
    @GetMapping("/item/list-by-group") @Operation(summary = "获得分组内的项目 BOM 物料")
    @PreAuthorize("@ss.hasPermission('bom:library:query')")
    public CommonResult<List<BomItemDO>> getItemsByGroup(@RequestParam Long groupId) {
        return success(service.getItemListByGroup(groupId));
    }
    @GetMapping("/item/import-template") @Operation(summary = "下载 BOM 物料导入模板")
    @PreAuthorize("@ss.hasPermission('bom:library:query')")
    public void importTemplate(HttpServletResponse response) throws IOException {
        List<BomItemImportExcelVO> rows = Arrays.asList(BomItemImportExcelVO.builder()
                .itemName("5015 风扇").itemCode("FAN-5015").specification("24V")
                .quantity(BigDecimal.ONE).unit("件").purpose("热端散热").build());
        ExcelUtils.write(response, "BOM物料导入模板.xlsx", "BOM物料", BomItemImportExcelVO.class, rows);
    }
    @PostMapping("/item/import") @Operation(summary = "导入 BOM 物料（不依赖 WMS）")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Integer> importItems(@RequestParam(required = false) Long projectId,
                                              @RequestParam(required = false) Long groupId,
                                              @RequestParam(required = false) Long variantId,
                                              @RequestParam("file") MultipartFile file) throws Exception {
        return success(service.importItems(projectId, groupId, variantId,
                ExcelUtils.read(file, BomItemImportExcelVO.class)));
    }
    @PutMapping("/wms-bom-binding/save") @Operation(summary = "保存项目 BOM 关联的多个 WMS BOM")
    @PreAuthorize("@ss.hasPermission('bom:library:write')")
    public CommonResult<Boolean> saveWmsBomBindings(@Valid @RequestBody BomWmsBomBindingSaveReqVO reqVO) {
        service.saveWmsBomBindings(reqVO);
        return success(true);
    }
    @GetMapping("/wms-bom-binding/list") @Operation(summary = "获得项目 BOM 关联的 WMS BOM")
    @PreAuthorize("@ss.hasPermission('bom:library:query')")
    public CommonResult<List<BomWmsBomBindingDO>> getWmsBomBindings(@RequestParam Long projectBomId) {
        return success(service.getWmsBomBindings(projectBomId));
    }
}

