package ca.quadrilateral.jua.game.impl;

import org.apache.commons.lang.builder.ToStringBuilder;
import ca.quadrilateral.jua.game.IMapCell;
import ca.quadrilateral.jua.game.IPartyPosition;
import ca.quadrilateral.jua.game.enums.FacingEnum;

public class PartyPosition implements IPartyPosition {
	private FacingEnum partyFacing = FacingEnum.Undefined;
	private int xPosition = 0;
	private int yPosition = 0;


	public PartyPosition() {}

	public PartyPosition(FacingEnum partyFacing, int xPosition, int yPosition) {
		this.partyFacing = partyFacing;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}

	@Override
	public IPartyPosition getDuplicatePosition() {
		final IPartyPosition clonedPartyPosition = new PartyPosition();
		clonedPartyPosition.setPartyFacing(this.getPartyFacing());
		clonedPartyPosition.setPosition(this.getXPosition(), this.getYPosition());
		return clonedPartyPosition;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("X Position", this.xPosition)
			.append("Y Position", this.yPosition)
			.append("Facing", this.partyFacing)
			.toString();
	}

	@Override
	public FacingEnum getPartyFacing() {
		return this.partyFacing;
	}

	@Override
	public void setPartyFacing(FacingEnum partyFacing) {
		this.partyFacing = partyFacing;
	}

	@Override
	public int getXPosition() {
		return this.xPosition;
	}

	@Override
	public int getYPosition() {
		return this.yPosition;
	}

	@Override
	public void setPosition(int xPosition, int yPosition) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}

	@Override
	public void setXPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	@Override
	public void setYPosition(int yPosition) {
		this.yPosition = yPosition;
	}

	@Override
	public void setPosition(IMapCell cell) {
		this.setPosition(cell.getCellX(), cell.getCellY());
	}

	@Override
	public void setPosition(IPartyPosition partyPosition) {
		this.setPartyFacing(partyPosition.getPartyFacing());
		this.setPosition(partyPosition.getXPosition(), partyPosition.getYPosition());
	}

}
