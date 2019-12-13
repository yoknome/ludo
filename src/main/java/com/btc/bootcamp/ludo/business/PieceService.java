package com.btc.bootcamp.ludo.business;

import com.btc.bootcamp.ludo.business.model.Piece;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class PieceService {

	public Optional<Piece> getPieceOnPosition(Set<Piece> pieces, int position) {
		return pieces.stream().filter(piece -> piece.getPosition().equals(position)).findAny();
	}

	public boolean allOnGoals(Set<Piece> pieces) {
		for (Piece piece : pieces) {
			if (piece.getPosition() >= 0) {
				return false;
			}
		}
		return true;
	}

}
