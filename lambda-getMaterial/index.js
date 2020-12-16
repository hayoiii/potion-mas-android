const mongoose = require('mongoose');
const MONGODB_URI = process.env.MONGODB_URI;

const materialSchema = mongoose.Schema({
    SKLL_IX_IRDNT_RAWMTRL:{
        type: String,
        required: false
    },
    LAST_UPDT_DTM:{
        type: String,
        required: false
    },
    
    INTK_UNIT: {
        type: String,
        required: false
    },
    IFTKN_ATNT_MATR_CN: {
        type: String,
        required: false
    },
    DAY_INTK_HIGHLIMIT: {
        type: String,
        required: false
    },
    PRDCT_NM: {
        type: String,
        required: false
    },
    CRET_DTM: {
        type: String,
        required: false
    },

    DAY_INTK_LOWLIMIT:{
        ype: String,
        required: false
    },
    PRIMARY_FNCLTY:{
        ype: String,
        required: false
    },
    INTK_MEMO:{
        ype: String,
        required: false
    }
});

let connection = null;

const connect = () => {
    if (connection && mongoose.connection.readyState === 1) 
        return Promise.resolve(connection); 
    return mongoose.connect(process.env.MONGODB_URI, { useNewUrlParser: true, authSource: "admin"}).then( 
        conn => { 
            connection = conn; 
            return connection; 
        } 
    ); 
};

/*Operation: GET*/
exports.handler = (event, context, callback) => {
    let Material = mongoose.model('material', materialSchema, 'material'); 
    let query = {};

    // 성분명으로 검색
    if (event.params.querystring.name) {
        query.PRDCT_NM = { $regex: '.*' + event.params.querystring.name + '.*' };
    }
    // 효능으로 검색
    if (event.params.querystring.effect) {
        query.PRIMARY_FNCLTY = { $regex: '.*' + event.params.querystring.effect + '.*' }
    }
    connect().then(() =>
        Material.find(query)
            .exec(function (error, materials) {
                if (error) {
                    error.statusCode = 401;
                    context.done(null, error);
                }
                else {
                    context.done(null, materials);
                }
            }));
}

    