const mongoose = require('mongoose');
const MONGODB_URI = process.env.MONGODB_URI;

const materialSchema = mongoose.Schema({
    SKLL_IX_IRDNT_RAWMTRL: {
        type: String,
        required: false
    },
    LAST_UPDT_DTM: {
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

    DAY_INTK_LOWLIMIT: {
        type: String,
        required: false
    },
    PRIMARY_FNCLTY: {
        type: String,
        required: false
    },
    INTK_MEMO: {
        type: String,
        required: false
    }
});

const customizedSchema = mongoose.Schema({
    id: {
        type: Number,
        required: true
    },

    name: {
        type: String,
        required: false
    },

    factory: {
        type: String,
        required: false
    },

    effect: {
        type: String,
        required: false
    },

    wayToTake: {
        type: String,
        required: false
    },

    remark: {
        type: String,
        required: false
    }
});

let connection = null;

const connect = () => {
    if (connection && mongoose.connection.readyState === 1)
        return Promise.resolve(connection);
    return mongoose.connect(process.env.MONGODB_URI, { useNewUrlParser: true, authSource: "admin" }).then(
        conn => {
            connection = conn;
            return connection;
        }
    );
};

exports.handler = (event, context, callback) => {
    let operation = event.httpMethod;
    let Material = mongoose.model('material', materialSchema, 'material');
    let Customized = mongoose.model('customized', customizedSchema, 'customized');
    let resource = event.resource;
    console.log(event);


    /***성분 검색****/
    if (resource == '/material') {
        /*Operation: GET*/
        let query = {};
        // 성분명으로 검색
        if (event.params.querystring.name) {
            query.PRDCT_NM = { $regex: '.*' + event.params.querystring.name + '.*' };
        }
        // 효능으로 검색
        if (event.params.querystring.effect) {
            query.PRIMARY_FNCLTY = { $regex: '.*' + event.params.querystring.effect + '.*' }
        }
        console.log("query",query);
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

    /*** 사용자 정의 아이템 ***/
    if (resource == '/customized') {
        switch (operation) {
            case 'POST':
                /*
                queryStringParameters: {}
                body: {"name":"추가할 아이템 이름", "effect":"주기능","factory":"제조사", "wayToTake":"복용 방법", "remark":"비고"} 
                설명: 사용자 정의 포션 추가 
                */
                let lastId = -1;
                connect().then(() =>
                    Customized.findOne({}).sort({ id: -1 }).exec(function (err, customized) {
                        if (err) {
                            err.statusCode = 401;
                            context.done(null, err);
                        }
                        else {
                            lastId = customized ? customized.id : 0;
                            const { name, factory, effect, wayToTake, remark } = JSON.parse(event.body);
                            
                            const newCustomized = new Customized({ name, factory, effect, wayToTake, remark });
                            newCustomized.id = lastId +1;
                            
                            // 새로운 아이템 등록. 
                            newCustomized.save(function (error, customized) {
                                if (error) {
                                    error.statusCode = 401;
                                    context.done(null, error);
                                }
                                else {
                                    context.done(null, { 'statusCode': 201, 'body': JSON.stringify(lastId + 1) });
                                }
                            });
                        }
                    })
                );
                break;

            case 'GET':
                //모든 item 가져오기
                if (event.params.querystring.id == -1) {
                    connect().then(() =>
                        Customized.find()
                            .sort({ id: -1 })
                            .exec(function (error, customized) {
                                if (error) {
                                    error.statusCode = 401;
                                    context.done(null, error); 
                                }
                                else {
                                    context.done(null, customized);
                                }
                            }));
                }
                //특정 item 가져오기
                else {
                    let id = event.params.querystring.id;
                    connect().then(() =>
                        Customized.find({ id: { '$eq': Number(id) } })
                            .exec(function (error, customized) {
                                if (error) {
                                    error.statusCode = 401;
                                    context.done(null, error); 
                                }
                                else {
                                    context.done(null, customized);
                                }
                            }));
                }
                break;
            case 'PATCH':
                /* 
                queryStringParameters: {"id": id(Number)}
                body:{"name":"추가할 아이템 이름", "effect":"주기능","factory":"제조사", "wayToTake":"복용 방법", "remark":"비고"}
                설명: 해당 id 정보 업데이트
                */
                let id = event.queryStringParameters.id;
                const update = JSON.parse(event.body);
                connect().then(() =>
                    Customized.findOneAndUpdate({ id: id }, update)
                        .exec(function (error, customized) {
                            if (error) {
                                error.statusCode = 401;
                                context.done(null, error); 
                            }
                            else {
                                context.done(null, { 'statusCode': 201, 'body': JSON.stringify(id) });
                            }
                        }));
                break;

            case 'DELETE':
                /* 
                queryStringParameters: {"id": id(Number)}
                body:{}
                설명: 해당 id 아이템 삭제
                */
                let target = event.queryStringParameters.id;
                connect().then(() =>
                    Customized.findOneAndRemove({ id: target })
                        .exec(function (error, customized) {
                            if (error) {
                                error.statusCode = 401;
                                context.done(null, error); 
                            }
                            else {
                                context.done(null, { 'statusCode': 201, 'body': JSON.stringify(target) });
                            }
                        }));
                break;

            default:
                callback(new Error(`Unrecognized operation "${operation}"`));
        }
    }
}

