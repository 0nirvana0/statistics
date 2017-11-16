package com.lq.main;

import java.io.File;
import java.util.List;

import com.lq.statistics.DataFilter;
import com.lq.statistics.ExcelDataGetter;
import com.lq.util.ExcelDataWriter;
import com.lq.util.TimeUtil;

public class Main {
	private ExcelDataGetter getter = new ExcelDataGetter();
	private ExcelDataWriter writer = new ExcelDataWriter();
	private DataFilter filter = new DataFilter();

	// 筛选没close
	public void dealNoClose(String filePath, String outPath) {
		List<Object[]> allRecord = getter.getData(filePath, 1);
		List<Object[]> notClose = filter.getNotClose(allRecord);
		writer.writeOut(notClose, outPath + "/data1.xlsx");
	}

	// 新的
	public void dealNew(String filePath, String outPath) {
		List<Object[]> allRecord = getter.getData(filePath, 1);
		List<Object[]> newStatus = filter.getNew(allRecord);
		writer.writeOut(newStatus, outPath + "/data2.xlsx");
	}

	// 超过10天没close
	public void dealD10NotClose(String filePath, String outPath) {
		List<Object[]> allRecord = getter.getData(filePath, 1);
		List<Object[]> D10NotClose = filter.getD10NotClose(allRecord);
		writer.writeOut(D10NotClose, outPath + "/data3.xlsx");
	}

	// 全部
	public void dealAll(String filePath, String outPath) {
		List<Object[]> allRecord = getter.getData(filePath, 1);
		List<Object[]> notClose = filter.getNotClose(allRecord);
		List<Object[]> newStatus = filter.getNew(allRecord);
		List<Object[]> D10NotClose = filter.getD10NotClose(allRecord);
		writer.writeOut(notClose, outPath + "/data1.xlsx");
		writer.writeOut(newStatus, outPath + "/data2.xlsx");
		writer.writeOut(D10NotClose, outPath + "/data3.xlsx");
	}

	public boolean createFolder(String path) {
		File file = new File(path);
		boolean flag = false;
		if (!file.exists()) {
			flag = file.mkdirs();
		} else {
			flag = true;
		}
		return flag;
	}

	public static void main(String[] args) {
		Main main = new Main();
		long lStart = System.currentTimeMillis();
		String path = "data/demo.xlsx";
		String outPath = "data/out/";
		main.dealAll(path, outPath);
		System.out.println(TimeUtil.formatDuring(lStart, System.currentTimeMillis()));
	}
}
