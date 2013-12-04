package ca.quadrilateral.jua.game.impl;

import org.junit.Assert;
import org.junit.Test;
import ca.quadrilateral.jua.game.enums.FacingEnum;
import ca.switchcase.commons.util.Pair;

public class TestDefaultMap {

	@Test
	public void adjustBoundsForValueShouldReturn0For0() {
		Assert.assertEquals(0, new DefaultMap().adjustValueForBounds(0, 15));
	}

	@Test
	public void adjustBoundsForValueShouldReturn14ForMinus1() {
		Assert.assertEquals(14, new DefaultMap().adjustValueForBounds(-1, 15));
	}

	@Test
	public void adjustBoundsForValueShouldReturn0ForMinus14() {
		Assert.assertEquals(0, new DefaultMap().adjustValueForBounds(-15, 15));
	}

	@Test
	public void adjustBoundsForValueShouldReturn1For1() {
		Assert.assertEquals(1, new DefaultMap().adjustValueForBounds(1, 15));
	}

	@Test
	public void adjustBoundsForValueShouldReturn0For15() {
		Assert.assertEquals(0, new DefaultMap().adjustValueForBounds(15, 15));
	}

	@Test
	public void adjustBoundsForValueShouldReturn1For16() {
		Assert.assertEquals(1, new DefaultMap().adjustValueForBounds(16, 15));
	}


	@Test
	public void calculateRelativePositionFacingNorthShouldReturnX4Y5() {
		final DefaultMap map = new DefaultMap();
		final Pair<Integer, Integer> result = map.calculateRelativePosition(2, 2, FacingEnum.North, 3, 2);
		Assert.assertEquals(new Integer(4), result.getFirst());
		Assert.assertEquals(new Integer(14), result.getSecond());
	}

	@Test
	public void calculateRelativePositionFacingEastShouldReturnX7Y4() {
		final DefaultMap map = new DefaultMap();
		final Pair<Integer, Integer> result = map.calculateRelativePosition(5, 1, FacingEnum.East, 2, -3);
		Assert.assertEquals(new Integer(7), result.getFirst());
		Assert.assertEquals(new Integer(13), result.getSecond());
	}

	@Test
	public void calculateRelativePositionFacingSouthShouldReturnX1Y2() {
		final DefaultMap map = new DefaultMap();
		final Pair<Integer, Integer> result = map.calculateRelativePosition(4, 4, FacingEnum.South, 2, 3);
		Assert.assertEquals(new Integer(1), result.getFirst());
		Assert.assertEquals(new Integer(6), result.getSecond());
	}

	@Test
	public void calculateRelativePositionFacingWesttShouldRetunX5Y5() {
		final DefaultMap map = new DefaultMap();
		final Pair<Integer, Integer> result = map.calculateRelativePosition(9, 9, FacingEnum.West, 4, -4);
		Assert.assertEquals(new Integer(5), result.getFirst());
		Assert.assertEquals(new Integer(13), result.getSecond());
	}

/*	@Test
	public void calculateRelativePositionOverlapWestShouldReturnX14Y5() {
		final DefaultMap map = new DefaultMap();
		final Pair<Integer, Integer> result = map.calculateRelativePosition(1, 5, FacingEnum.North, 0, -2);
		Assert.assertEquals(new Integer(14), result.getFirst());
		Assert.assertEquals(new Integer(5), result.getSecond());
	}

	@Test
	public void calculateRelativePositionOverlapNorthShouldReturnX5Y1() {
		final DefaultMap map = new DefaultMap();
		final Pair<Integer, Integer> result = map.calculateRelativePosition(5, 14, FacingEnum.North, 2, 0);
		Assert.assertEquals(new Integer(5), result.getFirst());
		Assert.assertEquals(new Integer(12), result.getSecond());
	}

	@Test
	public void calculateRelativePositionOverlapEastShouldReturnX0Y1() {
		final DefaultMap map = new DefaultMap();
		final Pair<Integer, Integer> result = map.calculateRelativePosition(14, 1, FacingEnum.North, 0, 1);
		Assert.assertEquals(new Integer(0), result.getFirst());
		Assert.assertEquals(new Integer(1), result.getSecond());
	}

	@Test
	public void calculateRelativePositionOverlapSouthShouldReturnX14Y14() {
		final DefaultMap map = new DefaultMap();
		final Pair<Integer, Integer> result = map.calculateRelativePosition(14, 1, FacingEnum.North, -2, 0);
		Assert.assertEquals(new Integer(12), result.getFirst());
		Assert.assertEquals(new Integer(14), result.getSecond());
	}*/


}
