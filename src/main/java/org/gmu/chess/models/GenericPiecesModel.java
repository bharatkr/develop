/*

 */

package org.gmu.chess.models;

import ca.watier.echechess.common.enums.Pieces;
import ca.watier.echechess.common.enums.Side;

public enum GenericPiecesModel {
    QUEEN, KNIGHT, ROOK, BISHOP;

    public static Pieces from(GenericPiecesModel model, Side side) {
        if (model == null || side == null) {
            return null;
        }

        switch (side) {
            case BLACK:
                return handleBlackSide(model);
            case WHITE:
                return handleWhiteSide(model);
        }

        return null;
    }

    private static Pieces handleBlackSide(GenericPiecesModel model) {
        switch (model) {
            case QUEEN:
                return Pieces.B_QUEEN;
            case KNIGHT:
                return Pieces.B_KNIGHT;
            case ROOK:
                return Pieces.B_ROOK;
            case BISHOP:
                return Pieces.B_BISHOP;
        }
        return null;
    }

    private static Pieces handleWhiteSide(GenericPiecesModel model) {
        switch (model) {
            case QUEEN:
                return Pieces.W_QUEEN;
            case KNIGHT:
                return Pieces.W_KNIGHT;
            case ROOK:
                return Pieces.W_ROOK;
            case BISHOP:
                return Pieces.W_BISHOP;
        }
        return null;
    }
}
