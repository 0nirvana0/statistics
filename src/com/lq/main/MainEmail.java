package com.lq.main;

import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;

import com.lq.statistics.DataFilter;
import com.lq.statistics.ExcelDataGetter;
import com.lq.util.ExcelDataWriter;
import com.lq.util.TimeUtil;

public class MainEmail {
	private ExcelDataGetter getter = new ExcelDataGetter();
	private ExcelDataWriter writer = new ExcelDataWriter();
	private DataFilter filter = new DataFilter();

	public static void main(String[] args) {
		MainEmail main = new MainEmail();
		long lStart = System.currentTimeMillis();
		String pathA = "data/统计/A.xlsx";
		String pathB = "data/统计/B.xlsx";
		String outPath = "C:/Users/liuqiang/Desktop";
		String status = "Colsed";
		main.statistic(pathA, pathB, status, outPath);
		System.out.println(TimeUtil.formatDuring(lStart, System.currentTimeMillis()));
	}

	public boolean statistic(String pathA, String pathB, String status, String outPath) {
		File fileA = new File(pathA);
		File fileB = new File(pathB);
		if (!fileA.exists()) {
			JOptionPane.showMessageDialog(null, fileA.getPath() + "文件不存在！");
			return false;
		}
		if (!fileB.exists()) {
			JOptionPane.showMessageDialog(null, fileB.getPath() + "文件不存在！");
			return false;
		}
		List<Object[]> recordA = getter.getData(pathA, 1);
		List<Object[]> recordB = getter.getData(pathB, 1);
		createFolder(outPath);
		List<Object[]> allRecord = filter.getStatistic(recordA, recordB, status);
		writer.writeOut(allRecord, outPath + "/data2.xlsx");
		return true;

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
}
