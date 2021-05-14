import Vue from 'vue'
import FileSaver from "file-saver";
import XLSX from "xlsx";

export default {
    /**
     * @param tableId 要导出的表格ID
     * @param fileName 文件导出名称
     * @param fileType 文件类型
     * @param rawFlag - true 导出的内容只做解析，不进行格式转换
     * @returns {*}
     */
    exportExcel(tableId,fileName,fileType,rawFlag) {
        let fix = document.querySelector('.el-table__fixed');
        let wb;
        /* 判断要导出的节点中是否有fixed的表格，如果有，转换excel时先将该dom移除，然后append回去 */
        if(fix){ 
            wb = XLSX.utils.table_to_book(document.querySelector("#"+tableId).removeChild(fix),{raw:rawFlag});
            document.querySelector("#"+tableId).appendChild(fix);
        }else{
            wb = XLSX.utils.table_to_book(document.querySelector("#"+tableId),{raw:rawFlag});
        }
        /* 获取二进制字符串作为输出 */
        let wbout = XLSX.write(wb, {
            bookType: "xlsx",
            bookSST: true,
            type: "array"
        });
        try {
            FileSaver.saveAs(
                //Blob 对象表示一个不可变、原始数据的类文件对象。
                //Blob 表示的不一定是JavaScript原生格式的数据。
                //File 接口基于Blob，继承了 blob 的功能并将其扩展使其支持用户系统上的文件。
                //返回一个新创建的 Blob 对象，其内容由参数中给定的数组串联组成。
                new Blob([wbout], { type: "application/octet-stream" }),
                //设置导出文件名称
                fileName + fileType
            );
        } catch (e) {
            if (typeof console !== "undefined") console.log(e, wbout);
        }
        return wbout;
    }
}