(echo "Checking De Morgan's laws")

(declare-const a Bool)
(declare-const b Bool)

(define-fun demorgan1 () Bool
    (= (not (and a b))
       (or (not a) (not b))))

(define-fun demorgan1_sub () Bool
    (= (and a b)
       (not (or (not a) (not b)))))

(define-fun demorgan2_sub () Bool
    (= (or a b)
       (not (and (not a) (not b)))))

(assert (not demorgan1))
;(assert (not demorgan1_sub))
;(assert (not demorgan2_sub))
(check-sat)
