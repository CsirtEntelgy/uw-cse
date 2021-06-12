#lang racket

(provide (all-defined-out))

(define (sequence spacing low high)
  (if (> low high)
      null
      (cons low (sequence spacing (+ low spacing) high))))

(define (string-append-map xs suffix)
  (map(lambda (String) (string-append String suffix)) xs))

(define (list-nth-mod xs n)
  (cond
    [(negative? n) (error "list-nth-mod: negative number")]
    [(null? xs) (error "list-nth-mod: empty list")]
    [else (car(list-tail xs (remainder n (length xs))))]))

(define (stream-for-k-steps s k)
  (let ([pr (s)])
    (if (= k 0)
        null
        (cons (car pr) (stream-for-k-steps (cdr pr) (- k 1))))))

(define funny-number-stream
  (letrec ([f (lambda (x)
                (if (= 0 (remainder x 6))
                    (cons (* x -1) (lambda () (f (+ x 1))))
                    (cons x (lambda () (f (+ x 1))))))])
    (lambda () (f 1))))

(define dan-then-dog
  (letrec ([f1 (lambda (x) (cons x (lambda () (f2 "dog.jpg"))))]
           [f2 (lambda (x) (cons x (lambda () (f1 "dan.jpg"))))])
    (lambda () (f1 "dan.jpg"))))

(define (stream-add-one s)
  (letrec ([f (lambda (x) (cons (cons 1 (car (x))) (lambda () (f (cdr (x))))))])
    (lambda () (f s))))

(define (cycle-lists xs ys)
  (letrec ([f (lambda (x) (cons (cons (list-nth-mod xs x) (list-nth-mod ys x))
                                (lambda () (f (+ 1 x)))))])
    (lambda () (f 0))))

(define (vector-assoc v vec)
  (letrec([f (lambda (x) (if (= x (vector-length vec))
                             #f
                             (if (pair? (vector-ref vec x))
                                 (if (equal? (car (vector-ref vec x)) v)
                                     (vector-ref vec x)
                                     (f (+ 1 x)))
                                 (f (+ 1 x)))))])
    (f 0)))

;If there already is a record computation on (assoc v xs) then
;returns the recorded result, else performs assoc and records its result until
;vector (our record) is full
(define (caching-assoc xs n)
  (letrec([vec (make-vector n [#f])]
          [counter 0]
          [f (lambda (x)
               (let ([ans (vector-assoc x vec)])
                 (if ans
                     (cdr ans)
                     (let ([new-ans (assoc x xs)])
                       (begin
                         (vector-set! vec counter (cons x new-ans))
                         (if(= counter (- (vector-length vec) 1))
                            (set! counter 0)
                            (set! counter counter))
                         new-ans)))))])
    f))

;Performs e2 while it is greater than e1
(define-syntax while-greater
  (syntax-rules ()
    [(while-greater e1 do e2)
     (letrec([f1 (lambda (x) (if (< e1 x)
                                 (f2 x)
                                 #t))]
             [f2 (lambda (x) (begin x (f1 e2)))])
       (f1 e2))]))