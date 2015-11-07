(declare-sort Char 0)
(declare-fun isDigit (Char) Bool)
(declare-fun isLetter (Char) Bool)

(declare-const a Char)

(define-fun blob () Bool
 (=> (and (isDigit a) (isLetter a))
     (isDigit a)))

(assert (not blob))
(check-sat)
