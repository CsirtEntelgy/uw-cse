;; CSE341, Programming Languages, Homework 5

#lang racket
(provide (all-defined-out)) ;; so we can put tests in a second file

;; definition of structures for MUPL programs - Do NOT change
(struct var  (string) #:transparent)  ;; a variable, e.g., (var "foo")
(struct int  (num)    #:transparent)  ;; a constant number, e.g., (int 17)
(struct add  (e1 e2)  #:transparent)  ;; add two expressions
(struct isgreater (e1 e2)    #:transparent) ;; if e1 > e2 then 1 else 0
(struct ifnz (e1 e2 e3) #:transparent) ;; if not zero e1 then e2 else e3
(struct fun  (nameopt formal body) #:transparent) ;; a recursive(?) 1-argument function
(struct call (funexp actual)       #:transparent) ;; function call
(struct mlet (var e body) #:transparent) ;; a local binding (let var = e in body) 
(struct apair   (e1 e2) #:transparent) ;; make a new pair
(struct first   (e)     #:transparent) ;; get first part of a pair
(struct second  (e)     #:transparent) ;; get second part of a pair
(struct munit   ()      #:transparent) ;; unit value -- good for ending a list
(struct ismunit (e)     #:transparent) ;; if e1 is unit then 1 else 0

;; a closure is not in "source" programs; it is what functions evaluate to
(struct closure (env fun) #:transparent) 

;; Problem 1
(define (racketlist->mupllist lst)
  (if(null? lst)
     (munit)
     (apair(car lst)(racketlist->mupllist (cdr lst)))))

(define (mupllist->racketlist lst)
  (if (munit? lst)
      null
      (cons (apair-e1 lst) (mupllist->racketlist(apair-e2 lst)))))

;; Problem 2
;; lookup a variable in an environment
;; Do NOT change this function
(define (envlookup env str)
  (cond [(null? env) (error "unbound variable during evaluation" str)]
        [(equal? (car (car env)) str) (cdr (car env))]
        [#t (envlookup (cdr env) str)]))

;; Do NOT change the two cases given to you.  
;; DO add more cases for other kinds of MUPL expressions.
;; We will test eval-under-env by calling it directly even though
;; "in real life" it would be a helper function of eval-exp.
(define (eval-under-env e env)
  (cond [(int? e)
         e]
        [(closure? e)
         e]
        [(var? e) 
         (envlookup env (var-string e))]
        [(munit? e)
         (munit)]
        [(add? e) 
         (let ([v1 (eval-under-env (add-e1 e) env)]
               [v2 (eval-under-env (add-e2 e) env)])
           (if (and (int? v1)
                    (int? v2))
               (int (+ (int-num v1) 
                       (int-num v2)))
               (error "MUPL addition applied to non-number")))]
        [(isgreater? e)
         (let ([v1 (eval-under-env (isgreater-e1 e) env)]
               [v2 (eval-under-env (isgreater-e2 e) env)])
           (if (and (int? v1)
                    (int? v2))
               (if (> (int-num v1) (int-num v2))
                   (int 1)
                   (int 0))
               (error "MUPL comparison applied to non-number")))]
        [(ifnz? e)
         (let ([v1 (eval-under-env (ifnz-e1 e) env)])
           (if (int? v1)
               (if(equal? (int-num v1) 0)
                  (eval-under-env (ifnz-e3 e) env)
                  (eval-under-env (ifnz-e2 e) env))
               (error "MUPL ifnz applied to non-number-first-argument")))]
        [(fun? e)
         (if (and (string? (fun-nameopt e)) (string? (fun-formal e)))
             (closure env e)
             ;;(closure (append (list (cons (fun-nameopt e) e))
                              ;;(list (cons (fun-formal e) (fun-body e)))
                              ;;env) e)
             (error "MUPL fun expression with non-string inputs"))]
        [(call? e)
         (let ([v1 (eval-under-env (call-funexp e) env)]
               [v2 (eval-under-env (call-actual e) env)])
           (if (closure? v1)
               (eval-under-env (fun-body(closure-fun v1))
                               (append(if (null? (fun-nameopt(closure-fun v1)))
                                          (list(cons (fun-formal(closure-fun v1)) v2))
                                          (append
                                           (list(cons (fun-nameopt(closure-fun v1)) v1))
                                           (list(cons (fun-formal(closure-fun v1)) v2))))
                                      (closure-env v1)))
               (error "MUPL call expression with non-closure argument")))]
        [(mlet? e)
         (if (string? (mlet-var e))
             (let ([v (eval-under-env (mlet-e e) env)])
               (eval-under-env (mlet-body e) (append env (list (cons (mlet-var e) v)))))
             (error "MUPL let expression bound to non-string"))]
        [(apair? e)
         (let ([v1 (eval-under-env (apair-e1 e) env)]
               [v2 (eval-under-env (apair-e2 e) env)])
           (apair v1 v2))]
        [(first? e)
         (let ([v (eval-under-env (first-e e) env)])
           (if (apair? v)
               (apair-e1 v)
               (error "MUPL first applied to non-apair")))]
        [(second? e)
         (let ([v (eval-under-env (second-e e) env)])
           (if (apair? v)
               (apair-e2 v)
               (error "MUPL second applied to non-apair")))]
        [(ismunit? e)
         (let ([v (eval-under-env (ismunit-e e) env)])
           (if (munit? v)
               (int 1)
               (int 0)))]
        [#t (error (format "bad MUPL expression: ~v" e))]))

;; Do NOT change
(define (eval-exp e)
  (eval-under-env e null))
        
;; Problem 3
(define (ifmunit e1 e2 e3)
  (if (munit? (eval-under-env e1 null))
      (eval-under-env e2 null)
      (eval-under-env e3 null)))

(define (mlet* bs e2)
  (letrec ([f (lambda (lst bindings)
                (if (null? lst)
                    bindings
                    (f (cdr lst)
                       (append bindings
                               (list (cons(car (car lst))
                                          (eval-under-env (cdr (car lst))
                                                           bindings)))))))])
    (eval-under-env e2 (f bs null))))

(define (ifeq e1 e2 e3 e4)
  (let ([_x (eval-under-env e1 null)]
        [_y (eval-under-env e2 null)])
    (if (and (int? _x) (int? _y))
        (if (= (int-num _x) (int-num _y))
            (eval-under-env e3 null)
            (eval-under-env e4 null))
        (error "ifeq first or second argument not MUPL int"))))

;; Problem 4
(define mupl-filter
  (fun "filter_name_1" "given_function"
       (fun "filter_name_2" "given_lst"
            (apair (call (var "given_function") (first (var "given_lst")))
                   (call (var "filter_name_2") (second (var "given_lst")))))))

(define mupl-all-gt
  (mlet "filter" mupl-filter
        (fun "take-int" "int"
             (fun "sort-int-list" "int-list"
                  (call (var "filter")
                        (ifnz (isgreater (first (var "int-list")) (var "int"))
                              (first (var "int-list"))
                              (munit)))))))

;; Challenge Problem
(struct fun-challenge (nameopt formal body freevars) #:transparent) ;; a recursive(?) 1-argument function

;; We will test this function directly, so it must do
;; as described in the assignment
(define (compute-free-vars e) "CHANGE")

;; Do NOT share code with eval-under-env because that will make grading
;; more difficult, so copy most of your interpreter here and make minor changes
(define (eval-under-env-c e env) "CHANGE")

;; Do NOT change this
(define (eval-exp-c e)
  (eval-under-env-c (compute-free-vars e) null))
