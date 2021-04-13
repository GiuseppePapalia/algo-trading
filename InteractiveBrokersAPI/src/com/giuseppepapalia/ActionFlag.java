package com.giuseppepapalia;

import org.apache.commons.lang3.tuple.Pair;

import com.giuseppepapalia.ibkr.BarData;
import com.giuseppepapalia.ibkr.ChartData;

public interface ActionFlag {

	public boolean buyFlag(Pair<BarData, ChartData> data);

	public boolean sellFlag(Pair<BarData, ChartData> data);
}
