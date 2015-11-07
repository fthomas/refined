(declare-const a Int)

(define-fun less () Bool
    (=> (> a 10)
        (> a 0)))

(define-fun interval () Bool
    (=> (and (> a 1) (< a 10))
        (> a 0)))

(assert (not interval))
(check-sat)
