/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.jspengine.el.parser;
import java.io.StringReader;
import javax.el.ELException;
public class ELParser/*@bgen(jjtree)*/implements ELParserTreeConstants, ELParserConstants {/*@bgen(jjtree)*/
  protected JJTELParserState jjtree = new JJTELParserState();public static Node parse(String ref) throws ELException
    {
        try {
                return (new ELParser(new StringReader(ref))).CompositeExpression();
        } catch (ParseException pe) {
                throw new ELException(pe.getMessage());
        }
    }

/*
 * CompositeExpression
 * Allow most flexible parsing, restrict by examining
 * type of returned node
 */
  final public AstCompositeExpression CompositeExpression() throws ParseException {
                                                                     /*@bgen(jjtree) CompositeExpression */
  AstCompositeExpression jjtn000 = new AstCompositeExpression(JJTCOMPOSITEEXPRESSION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      label_1:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LITERAL_EXPRESSION:
        case START_DYNAMIC_EXPRESSION:
        case START_DEFERRED_EXPRESSION:
          ;
          break;
        default:
          jj_la1[0] = jj_gen;
          break label_1;
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case START_DEFERRED_EXPRESSION:
          DeferredExpression();
          break;
        case START_DYNAMIC_EXPRESSION:
          DynamicExpression();
          break;
        case LITERAL_EXPRESSION:
          LiteralExpression();
          break;
        default:
          jj_la1[1] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      jj_consume_token(0);
                                                                                    jjtree.closeNodeScope(jjtn000, true);
                                                                                    jjtc000 = false;
                                                                                    {if (true) return jjtn000;}
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new Error("Missing return statement in function");
  }

/*
 * LiteralExpression
 * Non-EL Expression blocks
 */
  final public void LiteralExpression() throws ParseException {
                                               /*@bgen(jjtree) LiteralExpression */
                                                AstLiteralExpression jjtn000 = new AstLiteralExpression(JJTLITERALEXPRESSION);
                                                boolean jjtc000 = true;
                                                jjtree.openNodeScope(jjtn000);Token t = null;
    try {
      t = jj_consume_token(LITERAL_EXPRESSION);
                                 jjtree.closeNodeScope(jjtn000, true);
                                 jjtc000 = false;
                                 jjtn000.setImage(t.image);
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

/*
 * DeferredExpression
 * #{..} Expressions
 */
  final public void DeferredExpression() throws ParseException {
                                                 /*@bgen(jjtree) DeferredExpression */
  AstDeferredExpression jjtn000 = new AstDeferredExpression(JJTDEFERREDEXPRESSION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(START_DEFERRED_EXPRESSION);
      Expression();
      jj_consume_token(END_EXPRESSION);
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

/*
 * DynamicExpression
 * ${..} Expressions
 */
  final public void DynamicExpression() throws ParseException {
                                               /*@bgen(jjtree) DynamicExpression */
  AstDynamicExpression jjtn000 = new AstDynamicExpression(JJTDYNAMICEXPRESSION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(START_DYNAMIC_EXPRESSION);
      Expression();
      jj_consume_token(END_EXPRESSION);
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

/*
 * Expression
 * EL Expression Language Root, goes to Choice
 */
  final public void Expression() throws ParseException {
    Choice();
  }

/*
 * Choice
 * For Choice markup a ? b : c, then Or
 */
  final public void Choice() throws ParseException {
    Or();
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case QUESTIONMARK:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_2;
      }
      jj_consume_token(QUESTIONMARK);
      Or();
      jj_consume_token(COLON);
                                            AstChoice jjtn001 = new AstChoice(JJTCHOICE);
                                            boolean jjtc001 = true;
                                            jjtree.openNodeScope(jjtn001);
      try {
        Choice();
      } catch (Throwable jjte001) {
                                            if (jjtc001) {
                                              jjtree.clearNodeScope(jjtn001);
                                              jjtc001 = false;
                                            } else {
                                              jjtree.popNode();
                                            }
                                            if (jjte001 instanceof RuntimeException) {
                                              {if (true) throw (RuntimeException)jjte001;}
                                            }
                                            if (jjte001 instanceof ParseException) {
                                              {if (true) throw (ParseException)jjte001;}
                                            }
                                            {if (true) throw (Error)jjte001;}
      } finally {
                                            if (jjtc001) {
                                              jjtree.closeNodeScope(jjtn001,  3);
                                            }
      }
    }
  }

/*
 * Or
 * For 'or' '||', then And
 */
  final public void Or() throws ParseException {
    And();
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OR0:
      case OR1:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_3;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OR0:
        jj_consume_token(OR0);
        break;
      case OR1:
        jj_consume_token(OR1);
        break;
      default:
        jj_la1[4] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                               AstOr jjtn001 = new AstOr(JJTOR);
                               boolean jjtc001 = true;
                               jjtree.openNodeScope(jjtn001);
      try {
        And();
      } catch (Throwable jjte001) {
                               if (jjtc001) {
                                 jjtree.clearNodeScope(jjtn001);
                                 jjtc001 = false;
                               } else {
                                 jjtree.popNode();
                               }
                               if (jjte001 instanceof RuntimeException) {
                                 {if (true) throw (RuntimeException)jjte001;}
                               }
                               if (jjte001 instanceof ParseException) {
                                 {if (true) throw (ParseException)jjte001;}
                               }
                               {if (true) throw (Error)jjte001;}
      } finally {
                               if (jjtc001) {
                                 jjtree.closeNodeScope(jjtn001,  2);
                               }
      }
    }
  }

/*
 * And
 * For 'and' '&&', then Equality
 */
  final public void And() throws ParseException {
    Equality();
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case AND0:
      case AND1:
        ;
        break;
      default:
        jj_la1[5] = jj_gen;
        break label_4;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case AND0:
        jj_consume_token(AND0);
        break;
      case AND1:
        jj_consume_token(AND1);
        break;
      default:
        jj_la1[6] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                                      AstAnd jjtn001 = new AstAnd(JJTAND);
                                      boolean jjtc001 = true;
                                      jjtree.openNodeScope(jjtn001);
      try {
        Equality();
      } catch (Throwable jjte001) {
                                      if (jjtc001) {
                                        jjtree.clearNodeScope(jjtn001);
                                        jjtc001 = false;
                                      } else {
                                        jjtree.popNode();
                                      }
                                      if (jjte001 instanceof RuntimeException) {
                                        {if (true) throw (RuntimeException)jjte001;}
                                      }
                                      if (jjte001 instanceof ParseException) {
                                        {if (true) throw (ParseException)jjte001;}
                                      }
                                      {if (true) throw (Error)jjte001;}
      } finally {
                                      if (jjtc001) {
                                        jjtree.closeNodeScope(jjtn001,  2);
                                      }
      }
    }
  }

/*
 * Equality
 * For '==' 'eq' '!=' 'ne', then Compare
 */
  final public void Equality() throws ParseException {
    Compare();
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EQ0:
      case EQ1:
      case NE0:
      case NE1:
        ;
        break;
      default:
        jj_la1[7] = jj_gen;
        break label_5;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EQ0:
      case EQ1:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case EQ0:
          jj_consume_token(EQ0);
          break;
        case EQ1:
          jj_consume_token(EQ1);
          break;
        default:
          jj_la1[8] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
                                 AstEqual jjtn001 = new AstEqual(JJTEQUAL);
                                 boolean jjtc001 = true;
                                 jjtree.openNodeScope(jjtn001);
        try {
          Compare();
        } catch (Throwable jjte001) {
                                 if (jjtc001) {
                                   jjtree.clearNodeScope(jjtn001);
                                   jjtc001 = false;
                                 } else {
                                   jjtree.popNode();
                                 }
                                 if (jjte001 instanceof RuntimeException) {
                                   {if (true) throw (RuntimeException)jjte001;}
                                 }
                                 if (jjte001 instanceof ParseException) {
                                   {if (true) throw (ParseException)jjte001;}
                                 }
                                 {if (true) throw (Error)jjte001;}
        } finally {
                                 if (jjtc001) {
                                   jjtree.closeNodeScope(jjtn001,  2);
                                 }
        }
        break;
      case NE0:
      case NE1:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case NE0:
          jj_consume_token(NE0);
          break;
        case NE1:
          jj_consume_token(NE1);
          break;
        default:
          jj_la1[9] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
                                 AstNotEqual jjtn002 = new AstNotEqual(JJTNOTEQUAL);
                                 boolean jjtc002 = true;
                                 jjtree.openNodeScope(jjtn002);
        try {
          Compare();
        } catch (Throwable jjte002) {
                                 if (jjtc002) {
                                   jjtree.clearNodeScope(jjtn002);
                                   jjtc002 = false;
                                 } else {
                                   jjtree.popNode();
                                 }
                                 if (jjte002 instanceof RuntimeException) {
                                   {if (true) throw (RuntimeException)jjte002;}
                                 }
                                 if (jjte002 instanceof ParseException) {
                                   {if (true) throw (ParseException)jjte002;}
                                 }
                                 {if (true) throw (Error)jjte002;}
        } finally {
                                 if (jjtc002) {
                                   jjtree.closeNodeScope(jjtn002,  2);
                                 }
        }
        break;
      default:
        jj_la1[10] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

/*
 * Compare
 * For a bunch of them, then Math
 */
  final public void Compare() throws ParseException {
    Math();
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case GT0:
      case GT1:
      case LT0:
      case LT1:
      case GE0:
      case GE1:
      case LE0:
      case LE1:
        ;
        break;
      default:
        jj_la1[11] = jj_gen;
        break label_6;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LT0:
      case LT1:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LT0:
          jj_consume_token(LT0);
          break;
        case LT1:
          jj_consume_token(LT1);
          break;
        default:
          jj_la1[12] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
                                 AstLessThan jjtn001 = new AstLessThan(JJTLESSTHAN);
                                 boolean jjtc001 = true;
                                 jjtree.openNodeScope(jjtn001);
        try {
          Math();
        } catch (Throwable jjte001) {
                                 if (jjtc001) {
                                   jjtree.clearNodeScope(jjtn001);
                                   jjtc001 = false;
                                 } else {
                                   jjtree.popNode();
                                 }
                                 if (jjte001 instanceof RuntimeException) {
                                   {if (true) throw (RuntimeException)jjte001;}
                                 }
                                 if (jjte001 instanceof ParseException) {
                                   {if (true) throw (ParseException)jjte001;}
                                 }
                                 {if (true) throw (Error)jjte001;}
        } finally {
                                 if (jjtc001) {
                                   jjtree.closeNodeScope(jjtn001,  2);
                                 }
        }
        break;
      case GT0:
      case GT1:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case GT0:
          jj_consume_token(GT0);
          break;
        case GT1:
          jj_consume_token(GT1);
          break;
        default:
          jj_la1[13] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
                                 AstGreaterThan jjtn002 = new AstGreaterThan(JJTGREATERTHAN);
                                 boolean jjtc002 = true;
                                 jjtree.openNodeScope(jjtn002);
        try {
          Math();
        } catch (Throwable jjte002) {
                                 if (jjtc002) {
                                   jjtree.clearNodeScope(jjtn002);
                                   jjtc002 = false;
                                 } else {
                                   jjtree.popNode();
                                 }
                                 if (jjte002 instanceof RuntimeException) {
                                   {if (true) throw (RuntimeException)jjte002;}
                                 }
                                 if (jjte002 instanceof ParseException) {
                                   {if (true) throw (ParseException)jjte002;}
                                 }
                                 {if (true) throw (Error)jjte002;}
        } finally {
                                 if (jjtc002) {
                                   jjtree.closeNodeScope(jjtn002,  2);
                                 }
        }
        break;
      case LE0:
      case LE1:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LE0:
          jj_consume_token(LE0);
          break;
        case LE1:
          jj_consume_token(LE1);
          break;
        default:
          jj_la1[14] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
                                 AstLessThanEqual jjtn003 = new AstLessThanEqual(JJTLESSTHANEQUAL);
                                 boolean jjtc003 = true;
                                 jjtree.openNodeScope(jjtn003);
        try {
          Math();
        } catch (Throwable jjte003) {
                                 if (jjtc003) {
                                   jjtree.clearNodeScope(jjtn003);
                                   jjtc003 = false;
                                 } else {
                                   jjtree.popNode();
                                 }
                                 if (jjte003 instanceof RuntimeException) {
                                   {if (true) throw (RuntimeException)jjte003;}
                                 }
                                 if (jjte003 instanceof ParseException) {
                                   {if (true) throw (ParseException)jjte003;}
                                 }
                                 {if (true) throw (Error)jjte003;}
        } finally {
                                 if (jjtc003) {
                                   jjtree.closeNodeScope(jjtn003,  2);
                                 }
        }
        break;
      case GE0:
      case GE1:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case GE0:
          jj_consume_token(GE0);
          break;
        case GE1:
          jj_consume_token(GE1);
          break;
        default:
          jj_la1[15] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
                                 AstGreaterThanEqual jjtn004 = new AstGreaterThanEqual(JJTGREATERTHANEQUAL);
                                 boolean jjtc004 = true;
                                 jjtree.openNodeScope(jjtn004);
        try {
          Math();
        } catch (Throwable jjte004) {
                                 if (jjtc004) {
                                   jjtree.clearNodeScope(jjtn004);
                                   jjtc004 = false;
                                 } else {
                                   jjtree.popNode();
                                 }
                                 if (jjte004 instanceof RuntimeException) {
                                   {if (true) throw (RuntimeException)jjte004;}
                                 }
                                 if (jjte004 instanceof ParseException) {
                                   {if (true) throw (ParseException)jjte004;}
                                 }
                                 {if (true) throw (Error)jjte004;}
        } finally {
                                 if (jjtc004) {
                                   jjtree.closeNodeScope(jjtn004,  2);
                                 }
        }
        break;
      default:
        jj_la1[16] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

/*
 * Math
 * For '+' '-', then Multiplication
 */
  final public void Math() throws ParseException {
    Multiplication();
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
      case MINUS:
        ;
        break;
      default:
        jj_la1[17] = jj_gen;
        break label_7;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
        jj_consume_token(PLUS);
                          AstPlus jjtn001 = new AstPlus(JJTPLUS);
                          boolean jjtc001 = true;
                          jjtree.openNodeScope(jjtn001);
        try {
          Multiplication();
        } catch (Throwable jjte001) {
                          if (jjtc001) {
                            jjtree.clearNodeScope(jjtn001);
                            jjtc001 = false;
                          } else {
                            jjtree.popNode();
                          }
                          if (jjte001 instanceof RuntimeException) {
                            {if (true) throw (RuntimeException)jjte001;}
                          }
                          if (jjte001 instanceof ParseException) {
                            {if (true) throw (ParseException)jjte001;}
                          }
                          {if (true) throw (Error)jjte001;}
        } finally {
                          if (jjtc001) {
                            jjtree.closeNodeScope(jjtn001,  2);
                          }
        }
        break;
      case MINUS:
        jj_consume_token(MINUS);
                           AstMinus jjtn002 = new AstMinus(JJTMINUS);
                           boolean jjtc002 = true;
                           jjtree.openNodeScope(jjtn002);
        try {
          Multiplication();
        } catch (Throwable jjte002) {
                           if (jjtc002) {
                             jjtree.clearNodeScope(jjtn002);
                             jjtc002 = false;
                           } else {
                             jjtree.popNode();
                           }
                           if (jjte002 instanceof RuntimeException) {
                             {if (true) throw (RuntimeException)jjte002;}
                           }
                           if (jjte002 instanceof ParseException) {
                             {if (true) throw (ParseException)jjte002;}
                           }
                           {if (true) throw (Error)jjte002;}
        } finally {
                           if (jjtc002) {
                             jjtree.closeNodeScope(jjtn002,  2);
                           }
        }
        break;
      default:
        jj_la1[18] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

/*
 * Multiplication
 * For a bunch of them, then Unary
 */
  final public void Multiplication() throws ParseException {
    Unary();
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case MULT:
      case DIV0:
      case DIV1:
      case MOD0:
      case MOD1:
        ;
        break;
      default:
        jj_la1[19] = jj_gen;
        break label_8;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case MULT:
        jj_consume_token(MULT);
                          AstMult jjtn001 = new AstMult(JJTMULT);
                          boolean jjtc001 = true;
                          jjtree.openNodeScope(jjtn001);
        try {
          Unary();
        } catch (Throwable jjte001) {
                          if (jjtc001) {
                            jjtree.clearNodeScope(jjtn001);
                            jjtc001 = false;
                          } else {
                            jjtree.popNode();
                          }
                          if (jjte001 instanceof RuntimeException) {
                            {if (true) throw (RuntimeException)jjte001;}
                          }
                          if (jjte001 instanceof ParseException) {
                            {if (true) throw (ParseException)jjte001;}
                          }
                          {if (true) throw (Error)jjte001;}
        } finally {
                          if (jjtc001) {
                            jjtree.closeNodeScope(jjtn001,  2);
                          }
        }
        break;
      case DIV0:
      case DIV1:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case DIV0:
          jj_consume_token(DIV0);
          break;
        case DIV1:
          jj_consume_token(DIV1);
          break;
        default:
          jj_la1[20] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
                                   AstDiv jjtn002 = new AstDiv(JJTDIV);
                                   boolean jjtc002 = true;
                                   jjtree.openNodeScope(jjtn002);
        try {
          Unary();
        } catch (Throwable jjte002) {
                                   if (jjtc002) {
                                     jjtree.clearNodeScope(jjtn002);
                                     jjtc002 = false;
                                   } else {
                                     jjtree.popNode();
                                   }
                                   if (jjte002 instanceof RuntimeException) {
                                     {if (true) throw (RuntimeException)jjte002;}
                                   }
                                   if (jjte002 instanceof ParseException) {
                                     {if (true) throw (ParseException)jjte002;}
                                   }
                                   {if (true) throw (Error)jjte002;}
        } finally {
                                   if (jjtc002) {
                                     jjtree.closeNodeScope(jjtn002,  2);
                                   }
        }
        break;
      case MOD0:
      case MOD1:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case MOD0:
          jj_consume_token(MOD0);
          break;
        case MOD1:
          jj_consume_token(MOD1);
          break;
        default:
          jj_la1[21] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
                                   AstMod jjtn003 = new AstMod(JJTMOD);
                                   boolean jjtc003 = true;
                                   jjtree.openNodeScope(jjtn003);
        try {
          Unary();
        } catch (Throwable jjte003) {
                                   if (jjtc003) {
                                     jjtree.clearNodeScope(jjtn003);
                                     jjtc003 = false;
                                   } else {
                                     jjtree.popNode();
                                   }
                                   if (jjte003 instanceof RuntimeException) {
                                     {if (true) throw (RuntimeException)jjte003;}
                                   }
                                   if (jjte003 instanceof ParseException) {
                                     {if (true) throw (ParseException)jjte003;}
                                   }
                                   {if (true) throw (Error)jjte003;}
        } finally {
                                   if (jjtc003) {
                                     jjtree.closeNodeScope(jjtn003,  2);
                                   }
        }
        break;
      default:
        jj_la1[22] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

/*
 * Unary
 * For '-' '!' 'not' 'empty', then Value
 */
  final public void Unary() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case MINUS:
      jj_consume_token(MINUS);
                          AstNegative jjtn001 = new AstNegative(JJTNEGATIVE);
                          boolean jjtc001 = true;
                          jjtree.openNodeScope(jjtn001);
      try {
        Unary();
      } catch (Throwable jjte001) {
                          if (jjtc001) {
                            jjtree.clearNodeScope(jjtn001);
                            jjtc001 = false;
                          } else {
                            jjtree.popNode();
                          }
                          if (jjte001 instanceof RuntimeException) {
                            {if (true) throw (RuntimeException)jjte001;}
                          }
                          if (jjte001 instanceof ParseException) {
                            {if (true) throw (ParseException)jjte001;}
                          }
                          {if (true) throw (Error)jjte001;}
      } finally {
                          if (jjtc001) {
                            jjtree.closeNodeScope(jjtn001, true);
                          }
      }
      break;
    case NOT0:
    case NOT1:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NOT0:
        jj_consume_token(NOT0);
        break;
      case NOT1:
        jj_consume_token(NOT1);
        break;
      default:
        jj_la1[23] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                                  AstNot jjtn002 = new AstNot(JJTNOT);
                                  boolean jjtc002 = true;
                                  jjtree.openNodeScope(jjtn002);
      try {
        Unary();
      } catch (Throwable jjte002) {
                                  if (jjtc002) {
                                    jjtree.clearNodeScope(jjtn002);
                                    jjtc002 = false;
                                  } else {
                                    jjtree.popNode();
                                  }
                                  if (jjte002 instanceof RuntimeException) {
                                    {if (true) throw (RuntimeException)jjte002;}
                                  }
                                  if (jjte002 instanceof ParseException) {
                                    {if (true) throw (ParseException)jjte002;}
                                  }
                                  {if (true) throw (Error)jjte002;}
      } finally {
                                  if (jjtc002) {
                                    jjtree.closeNodeScope(jjtn002, true);
                                  }
      }
      break;
    case EMPTY:
      jj_consume_token(EMPTY);
                          AstEmpty jjtn003 = new AstEmpty(JJTEMPTY);
                          boolean jjtc003 = true;
                          jjtree.openNodeScope(jjtn003);
      try {
        Unary();
      } catch (Throwable jjte003) {
                          if (jjtc003) {
                            jjtree.clearNodeScope(jjtn003);
                            jjtc003 = false;
                          } else {
                            jjtree.popNode();
                          }
                          if (jjte003 instanceof RuntimeException) {
                            {if (true) throw (RuntimeException)jjte003;}
                          }
                          if (jjte003 instanceof ParseException) {
                            {if (true) throw (ParseException)jjte003;}
                          }
                          {if (true) throw (Error)jjte003;}
      } finally {
                          if (jjtc003) {
                            jjtree.closeNodeScope(jjtn003, true);
                          }
      }
      break;
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case STRING_LITERAL:
    case TRUE:
    case FALSE:
    case NULL:
    case LPAREN:
    case IDENTIFIER:
      Value();
      break;
    default:
      jj_la1[24] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

/*
 * Value
 * Defines Prefix plus zero or more Suffixes
 */
  final public void Value() throws ParseException {
          AstValue jjtn001 = new AstValue(JJTVALUE);
          boolean jjtc001 = true;
          jjtree.openNodeScope(jjtn001);
    try {
      ValuePrefix();
      label_9:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case DOT:
        case LBRACK:
          ;
          break;
        default:
          jj_la1[25] = jj_gen;
          break label_9;
        }
        ValueSuffix();
      }
    } catch (Throwable jjte001) {
          if (jjtc001) {
            jjtree.clearNodeScope(jjtn001);
            jjtc001 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte001 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte001;}
          }
          if (jjte001 instanceof ParseException) {
            {if (true) throw (ParseException)jjte001;}
          }
          {if (true) throw (Error)jjte001;}
    } finally {
          if (jjtc001) {
            jjtree.closeNodeScope(jjtn001, jjtree.nodeArity() > 1);
          }
    }
  }

/*
 * ValuePrefix
 * For Literals, Variables, and Functions
 */
  final public void ValuePrefix() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case STRING_LITERAL:
    case TRUE:
    case FALSE:
    case NULL:
      Literal();
      break;
    case LPAREN:
    case IDENTIFIER:
      NonLiteral();
      break;
    default:
      jj_la1[26] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

/*
 * ValueSuffix
 * Either dot or bracket notation
 */
  final public void ValueSuffix() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case DOT:
      DotSuffix();
      break;
    case LBRACK:
      BracketSuffix();
      break;
    default:
      jj_la1[27] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

/*
 * DotSuffix
 * Dot Property
 */
  final public void DotSuffix() throws ParseException {
                               /*@bgen(jjtree) DotSuffix */
                                AstDotSuffix jjtn000 = new AstDotSuffix(JJTDOTSUFFIX);
                                boolean jjtc000 = true;
                                jjtree.openNodeScope(jjtn000);Token t = null;
    try {
      jj_consume_token(DOT);
      t = jj_consume_token(IDENTIFIER);
                               jjtree.closeNodeScope(jjtn000, true);
                               jjtc000 = false;
                               jjtn000.setImage(t.image);
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

/*
 * BracketSuffix
 * Sub Expression Suffix
 */
  final public void BracketSuffix() throws ParseException {
                                       /*@bgen(jjtree) BracketSuffix */
  AstBracketSuffix jjtn000 = new AstBracketSuffix(JJTBRACKETSUFFIX);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(LBRACK);
      Expression();
      jj_consume_token(RBRACK);
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

/*
 * NonLiteral
 * For Grouped Operations, Identifiers, and Functions
 */
  final public void NonLiteral() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LPAREN:
      jj_consume_token(LPAREN);
      Expression();
      jj_consume_token(RPAREN);
      break;
    default:
      jj_la1[28] = jj_gen;
      if (jj_2_1(2)) {
        Function();
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case IDENTIFIER:
          Identifier();
          break;
        default:
          jj_la1[29] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    }
  }

/*
 * Identifier
 * Java Language Identifier
 */
  final public void Identifier() throws ParseException {
                                 /*@bgen(jjtree) Identifier */
                                  AstIdentifier jjtn000 = new AstIdentifier(JJTIDENTIFIER);
                                  boolean jjtc000 = true;
                                  jjtree.openNodeScope(jjtn000);Token t = null;
    try {
      t = jj_consume_token(IDENTIFIER);
                         jjtree.closeNodeScope(jjtn000, true);
                         jjtc000 = false;
                         jjtn000.setImage(t.image);
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

/*
 * Function
 * Namespace:Name(a,b,c)
 */
  final public void Function() throws ParseException {
 /*@bgen(jjtree) Function */
        AstFunction jjtn000 = new AstFunction(JJTFUNCTION);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token t0 = null;
        Token t1 = null;
    try {
      t0 = jj_consume_token(IDENTIFIER);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FUNCTIONSUFFIX:
        t1 = jj_consume_token(FUNCTIONSUFFIX);
        break;
      default:
        jj_la1[30] = jj_gen;
        ;
      }
                if (t1 != null) {
                        jjtn000.setPrefix(t0.image);
                        jjtn000.setLocalName(t1.image.substring(1));
                } else {
                        jjtn000.setLocalName(t0.image);
                }
      jj_consume_token(LPAREN);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case STRING_LITERAL:
      case TRUE:
      case FALSE:
      case NULL:
      case LPAREN:
      case NOT0:
      case NOT1:
      case EMPTY:
      case MINUS:
      case IDENTIFIER:
        Expression();
        label_10:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case COMMA:
            ;
            break;
          default:
            jj_la1[31] = jj_gen;
            break label_10;
          }
          jj_consume_token(COMMA);
          Expression();
        }
        break;
      default:
        jj_la1[32] = jj_gen;
        ;
      }
      jj_consume_token(RPAREN);
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

/*
 * Literal
 * Reserved Keywords
 */
  final public void Literal() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TRUE:
    case FALSE:
      Boolean();
      break;
    case FLOATING_POINT_LITERAL:
      FloatingPoint();
      break;
    case INTEGER_LITERAL:
      Integer();
      break;
    case STRING_LITERAL:
      String();
      break;
    case NULL:
      Null();
      break;
    default:
      jj_la1[33] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

/*
 * Boolean
 * For 'true' 'false'
 */
  final public void Boolean() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TRUE:
          AstTrue jjtn001 = new AstTrue(JJTTRUE);
          boolean jjtc001 = true;
          jjtree.openNodeScope(jjtn001);
      try {
        jj_consume_token(TRUE);
      } finally {
          if (jjtc001) {
            jjtree.closeNodeScope(jjtn001, true);
          }
      }
      break;
    case FALSE:
            AstFalse jjtn002 = new AstFalse(JJTFALSE);
            boolean jjtc002 = true;
            jjtree.openNodeScope(jjtn002);
      try {
        jj_consume_token(FALSE);
      } finally {
            if (jjtc002) {
              jjtree.closeNodeScope(jjtn002, true);
            }
      }
      break;
    default:
      jj_la1[34] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

/*
 * FloatinPoint
 * For Decimal and Floating Point Literals
 */
  final public void FloatingPoint() throws ParseException {
                                       /*@bgen(jjtree) FloatingPoint */
                                        AstFloatingPoint jjtn000 = new AstFloatingPoint(JJTFLOATINGPOINT);
                                        boolean jjtc000 = true;
                                        jjtree.openNodeScope(jjtn000);Token t = null;
    try {
      t = jj_consume_token(FLOATING_POINT_LITERAL);
                                     jjtree.closeNodeScope(jjtn000, true);
                                     jjtc000 = false;
                                     jjtn000.setImage(t.image);
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

/*
 * Integer
 * For Simple Numeric Literals
 */
  final public void Integer() throws ParseException {
                           /*@bgen(jjtree) Integer */
                            AstInteger jjtn000 = new AstInteger(JJTINTEGER);
                            boolean jjtc000 = true;
                            jjtree.openNodeScope(jjtn000);Token t = null;
    try {
      t = jj_consume_token(INTEGER_LITERAL);
                              jjtree.closeNodeScope(jjtn000, true);
                              jjtc000 = false;
                              jjtn000.setImage(t.image);
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

/*
 * String
 * For Quoted Literals
 */
  final public void String() throws ParseException {
                         /*@bgen(jjtree) String */
                          AstString jjtn000 = new AstString(JJTSTRING);
                          boolean jjtc000 = true;
                          jjtree.openNodeScope(jjtn000);Token t = null;
    try {
      t = jj_consume_token(STRING_LITERAL);
                             jjtree.closeNodeScope(jjtn000, true);
                             jjtc000 = false;
                             jjtn000.setImage(t.image);
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

/*
 * Null
 * For 'null'
 */
  final public void Null() throws ParseException {
                     /*@bgen(jjtree) Null */
  AstNull jjtn000 = new AstNull(JJTNULL);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(NULL);
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  final private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  final private boolean jj_3_1() {
    if (jj_3R_11()) return true;
    return false;
  }

  final private boolean jj_3R_11() {
    if (jj_scan_token(IDENTIFIER)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(54)) jj_scanpos = xsp;
    if (jj_scan_token(LPAREN)) return true;
    return false;
  }

  public ELParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  public boolean lookingAhead = false;
  private boolean jj_semLA;
  private int jj_gen;
  final private int[] jj_la1 = new int[35];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_0();
      jj_la1_1();
   }
   private static void jj_la1_0() {
      jj_la1_0 = new int[] {0xe,0xe,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0xfe000000,0x18000000,0x6000000,0x80000000,0x60000000,0xfe000000,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x9d600,0x240000,0x9d600,0x240000,0x80000,0x0,0x0,0x1000000,0x9d600,0x1d600,0xc000,};
   }
   private static void jj_la1_1() {
      jj_la1_1 = new int[] {0x0,0x0,0x10000,0x600,0x600,0x180,0x180,0x1e,0x6,0x18,0x1e,0x1,0x0,0x0,0x1,0x0,0x1,0xc000,0xc000,0x1e2000,0x60000,0x180000,0x1e2000,0x60,0x208860,0x0,0x200000,0x0,0x0,0x200000,0x400000,0x0,0x208860,0x0,0x0,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[1];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  public ELParser(java.io.InputStream stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ELParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 35; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 35; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public ELParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ELParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 35; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 35; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public ELParser(ELParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 35; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(ELParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 35; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  final private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = lookingAhead ? jj_scanpos : token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      boolean exists = false;
      for (java.util.Enumeration e = jj_expentries.elements(); e.hasMoreElements();) {
        int[] oldentry = (int[])(e.nextElement());
        if (oldentry.length == jj_expentry.length) {
          exists = true;
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              exists = false;
              break;
            }
          }
          if (exists) break;
        }
      }
      if (!exists) jj_expentries.addElement(jj_expentry);
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[59];
    for (int i = 0; i < 59; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 35; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 59; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

  final private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 1; i++) {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
          }
        }
        p = p.next;
      } while (p != null);
    }
    jj_rescan = false;
  }

  final private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
