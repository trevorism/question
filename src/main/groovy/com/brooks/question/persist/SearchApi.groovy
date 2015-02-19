package com.brooks.question.persist

import com.brooks.question.api.IdGenerator
import com.brooks.question.api.Lucene
import com.brooks.question.convert.QuestionDocumentConverter
import com.brooks.question.model.Answer
import com.brooks.question.model.Question
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.index.Term
import org.apache.lucene.search.BooleanClause
import org.apache.lucene.search.BooleanQuery
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.MatchAllDocsQuery
import org.apache.lucene.search.Query
import org.apache.lucene.search.ScoreDoc
import org.apache.lucene.search.Sort
import org.apache.lucene.search.SortField
import org.apache.lucene.search.TermQuery
import org.apache.lucene.search.TopDocs
import org.apache.lucene.search.TopFieldDocs
import org.apache.lucene.search.TopScoreDocCollector
import org.apache.lucene.search.WildcardQuery
import org.apache.lucene.util.Version

/**
 * @author tbrooks
 */
class SearchApi implements QuestionApi{

    public static final int MAX_DOCUMENTS = 10
    private Lucene lucene

    SearchApi(Lucene lucene){
        this.lucene = lucene
        initializeIndexGenerator(lucene)
    }

    @Override
    Question getQuestion(String id) {
        IndexReader reader
        try{
            reader = DirectoryReader.open(lucene.directory)
            return getQuestionByIdFromIndex(reader, id)
        }catch(Exception e){
            return Question.NULL_QUESTION
        }
        finally{
            if(reader)
                reader.close()
        }
    }

    @Override
    String createQuestion(Question question) {
        IndexWriter indexWriter = getIndexWriter()

        question.id = IdGenerator.instance.nextId.toString()
        question.dateAsked = new Date()
        Document document = QuestionDocumentConverter.convert(question)

        indexWriter.addDocument(document)
        indexWriter.close()
        return question.id
    }

    @Override
    void updateQuestion(String id, String questionText) {
        IndexWriter indexWriter = getIndexWriter()
        Question question = getQuestion(id)
        question.questionText = questionText;

        Document document = QuestionDocumentConverter.convert(question)
        indexWriter.updateDocument(createTerm(question.id), document)
        indexWriter.close()

    }

    @Override
    void answerQuestion(String id, String answerText) {
        Question question = getQuestion(id)
        Answer answer = new Answer(answerText: answerText, dateAnswered: new Date())
        updateAnswer(question, answer)
    }

    @Override
    List<Question> searchQuestions(String queryString) {
        DirectoryReader reader
        try{
            reader = DirectoryReader.open(lucene.directory)
            IndexSearcher indexSearcher = new IndexSearcher(reader)

            queryString = "*$queryString*"

            BooleanQuery query = new BooleanQuery()
            query.add(new BooleanClause(new WildcardQuery(new Term("name", queryString)), BooleanClause.Occur.SHOULD))
            query.add(new BooleanClause(new WildcardQuery(new Term("questionText", queryString)), BooleanClause.Occur.SHOULD))
            query.add(new BooleanClause(new WildcardQuery(new Term("answerText", queryString)), BooleanClause.Occur.SHOULD))

            TopScoreDocCollector collector = TopScoreDocCollector.create(MAX_DOCUMENTS, true);
            indexSearcher.search(query, collector)

            TopDocs search = collector.topDocs()

            return search.scoreDocs.collect{ ScoreDoc scoreDoc->
                questionFromScoreDoc(reader, scoreDoc)
            }

        }catch(Exception e){
            e.printStackTrace()
            return []
        }
        finally{
            if(reader)
                reader.close()
        }    }

    @Override
    List<Question> getUnansweredQuestions() {
        DirectoryReader reader
        try{
            reader = DirectoryReader.open(lucene.directory)
            IndexSearcher indexSearcher = new IndexSearcher(reader)

            Query query = new TermQuery(new Term("answerText", QuestionDocumentConverter.UNANSWERED))
            TopDocs search = indexSearcher.search(query, MAX_DOCUMENTS, new Sort(new SortField("dateAsked", SortField.Type.STRING)))

            return search.scoreDocs.collect{ ScoreDoc scoreDoc->
                questionFromScoreDoc(reader, scoreDoc)
            }

        }catch(Exception e){
            e.printStackTrace()
            return []
        }
        finally{
            if(reader)
                reader.close()
        }
    }

    @Override
    List<Question> getLatestAnsweredQuestions() {
        DirectoryReader reader
        try{
            reader = DirectoryReader.open(lucene.directory)
            IndexSearcher indexSearcher = new IndexSearcher(reader)

            BooleanQuery query = new BooleanQuery()
            query.add(new BooleanClause(new MatchAllDocsQuery(), BooleanClause.Occur.SHOULD));
            query.add(new BooleanClause(new TermQuery(new Term("answerText", QuestionDocumentConverter.UNANSWERED)), BooleanClause.Occur.MUST_NOT))
            TopDocs search = indexSearcher.search(query, MAX_DOCUMENTS, new Sort(new SortField("dateAnswered", SortField.Type.STRING, true)))

            return search.scoreDocs.collect{ ScoreDoc scoreDoc->
                questionFromScoreDoc(reader, scoreDoc)
            }

        }catch(Exception e){
            e.printStackTrace()
            return []
        }
        finally{
            if(reader)
                reader.close()
        }
    }

    @Override
    void deleteQuestion(String id) {
        IndexWriter indexWriter = getIndexWriter()
        indexWriter.deleteDocuments(createTerm(id))
        indexWriter.close()
    }

    @Override
    void deleteAnswer(String id) {
        Question question = getQuestion(id)
        updateAnswer(question, null)
    }

    private initializeIndexGenerator(Lucene lucene) {
        IndexReader reader
        try {
            reader = DirectoryReader.open(lucene.directory)
            IdGenerator.instance.setId(reader.maxDoc())

        } catch (Exception) {

        }
        finally {
            if (reader)
                reader.close()
        }
    }

    private Question getQuestionByIdFromIndex(DirectoryReader reader, String id) {
        IndexSearcher indexSearcher = new IndexSearcher(reader)
        Query query = new TermQuery(createTerm(id))
        TopDocs docs = indexSearcher.search(query, 1)
        ScoreDoc scoreDoc = docs.scoreDocs[0]
        return questionFromScoreDoc(reader, scoreDoc)
    }

    private Question questionFromScoreDoc(DirectoryReader reader, ScoreDoc scoreDoc) {
        Document document = reader.document(scoreDoc.doc)
        return QuestionDocumentConverter.convert(document)
    }

    private Term createTerm(String id) {
        new Term("id", id)
    }

    private IndexWriter getIndexWriter() {
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_3)
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer)
        IndexWriter indexWriter = new IndexWriter(lucene.directory, config)
        return indexWriter
    }

    private updateAnswer(Question question, Answer answer) {
        IndexWriter indexWriter = getIndexWriter()
        question.answer = answer

        Document document = QuestionDocumentConverter.convert(question)
        indexWriter.updateDocument(createTerm(question.id), document)
        indexWriter.close()
    }


}
